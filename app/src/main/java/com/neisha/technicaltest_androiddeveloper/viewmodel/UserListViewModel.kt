package com.neisha.technicaltest_androiddeveloper.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.neisha.technicaltest_androiddeveloper.analytics.AnalyticsHelper
import com.neisha.technicaltest_androiddeveloper.domain.model.City
import com.neisha.technicaltest_androiddeveloper.domain.model.SortOption
import com.neisha.technicaltest_androiddeveloper.domain.model.User
import com.neisha.technicaltest_androiddeveloper.domain.usecase.AddUserUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.GetCitiesUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.GetUsersUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.RefreshCitiesUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.RefreshUsersUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.SearchUsersUseCase
import com.neisha.technicaltest_androiddeveloper.util.ConnectivityObserver
import com.neisha.technicaltest_androiddeveloper.worker.SyncWorker
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class UserListUiState(
    val users: List<User> = emptyList(),
    val cities: List<City> = emptyList(),
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedCities: Set<String> = emptySet(),
    val sortOption: SortOption = SortOption.NAME_ASC,
    val totalUsers: Int = 0
)

sealed class AddUserUiState {
    object Idle : AddUserUiState()
    object Loading : AddUserUiState()
    object Success : AddUserUiState()
    data class Error(val message: String) : AddUserUiState()
}

@OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
@HiltViewModel
class UserListViewModel @Inject constructor(
    private val searchUsersUseCase: SearchUsersUseCase,
    private val addUserUseCase: AddUserUseCase,
    private val getUsersUseCase: GetUsersUseCase,
    private val refreshUsersUseCase: RefreshUsersUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val refreshCitiesUseCase: RefreshCitiesUseCase,
    private val analyticsHelper: AnalyticsHelper,
    @ApplicationContext private val context: Context,
    private val connectivityObserver: ConnectivityObserver
) : ViewModel() {

    private val workManager = WorkManager.getInstance(context)

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    private val _selectedCities = MutableStateFlow<Set<String>>(emptySet())
    private val _sortOption = MutableStateFlow(SortOption.NAME_ASC)
    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _addUserState = MutableStateFlow<AddUserUiState>(AddUserUiState.Idle)

    val addUserState: StateFlow<AddUserUiState> = _addUserState.asStateFlow()

    val uiState: StateFlow<UserListUiState> = combine(
        _searchQuery.debounce(300),
        _selectedCities,
        _sortOption,
        _isLoading,
        _isRefreshing,
        _error,
        getCitiesUseCase()
    ) { values ->
        val query = values[0] as String
        val cities = values[1] as Set<*>
        val sort = values[2] as SortOption
        val loading = values[3] as Boolean
        val refreshing = values[4] as Boolean
        val error = values[5] as String?
        @Suppress("UNCHECKED_CAST")
        val cityList = values[6] as List<City>
        val selectedCities = cities as Set<String>
        Triple(Triple(query, selectedCities, sort), Triple(loading, refreshing, error), cityList)
    }.flatMapLatest { (filters, status, cities) ->
        val (query, selectedCities, sort) = filters
        val (loading, refreshing, error) = status
        searchUsersUseCase(query, selectedCities, sort).combine(
            kotlinx.coroutines.flow.flowOf(cities)
        ) { users, cityList ->
            UserListUiState(
                users = users,
                cities = cityList,
                isLoading = loading,
                isRefreshing = refreshing,
                error = error,
                searchQuery = query,
                selectedCities = selectedCities,
                sortOption = sort
            )
        }
    }.combine(getUsersUseCase()) { state, allUsers ->
        state.copy(totalUsers = allUsers.size)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserListUiState(isLoading = true)
    )

    init {
        schedulePeriodicSync()
        observeConnectivity()
        initialLoad()
    }

    private fun initialLoad() {
        viewModelScope.launch {
            _isLoading.update { true }
            try {
                refreshUsersUseCase()
                refreshCitiesUseCase()
            } catch (e: Exception) {
                _error.update { "Gagal memuat data. Menampilkan data offline." }
            } finally {
                _isLoading.update { false }
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.update { true }
            _error.update { null }
            try {
                refreshUsersUseCase()
                refreshCitiesUseCase()
            } catch (e: Exception) {
                _error.update { "Gagal memperbarui data." }
            } finally {
                _isRefreshing.update { false }
            }
        }
    }

    fun onSearchQueryChange(query: String) {
        _searchQuery.update { query }
        if (query.isNotBlank()) analyticsHelper.logSearch(query)
    }

    fun onCityFilterChange(city: String) {
        _selectedCities.update { current ->
            if (city.isBlank()) emptySet()
            else if (city in current) current - city
            else current + city
        }
        if (city.isNotBlank()) analyticsHelper.logFilterApplied(city)
    }

    fun onSortToggle() {
        val current = _sortOption.value
        _sortOption.update {
            when (current) {
                SortOption.NAME_ASC -> SortOption.NAME_DESC
                SortOption.NAME_DESC -> SortOption.NAME_ASC
            }
        }
    }

    fun clearError() {
        _error.update { null }
    }

    fun resetAddUserState() {
        _addUserState.update { AddUserUiState.Idle }
    }

    fun addUser(user: User) {
        viewModelScope.launch {
            _addUserState.update { AddUserUiState.Loading }
            val result = addUserUseCase(user)
            if (result.isSuccess) {
                analyticsHelper.logUserAdded()
                refreshUsersUseCase()
                _addUserState.update { AddUserUiState.Success }
            } else {
                val errorMsg = result.exceptionOrNull()
                val message = when {
                    errorMsg == null -> "Gagal menambahkan user"
                    errorMsg.message?.contains("Email sudah terdaftar") == true -> "Email sudah terdaftar"
                    errorMsg is retrofit2.HttpException -> {
                        val code = errorMsg.code()
                        val errorBody = try { errorMsg.response()?.errorBody()?.string() } catch (_: Exception) { null }
                        if (!errorBody.isNullOrBlank()) errorBody
                        else if (code == 400) "Data yang dikirim tidak valid (400). Periksa isian Anda."
                        else if (code == 409) "Data sudah ada di server (409)."
                        else "Gagal menambahkan user (kode $code)."
                    }
                    errorMsg is java.net.ConnectException || errorMsg is java.net.UnknownHostException ->
                        "Tidak ada koneksi internet. Periksa jaringan Anda."
                    errorMsg is java.net.SocketTimeoutException ->
                        "Koneksi timeout. Coba lagi."
                    else -> "Gagal menambahkan user. Coba lagi."
                }
                _addUserState.update { AddUserUiState.Error(message) }
            }
        }
    }

    private fun schedulePeriodicSync() {
        val request = PeriodicWorkRequestBuilder<SyncWorker>(15, java.util.concurrent.TimeUnit.MINUTES)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueueUniquePeriodicWork(
            SyncWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            request
        )
    }

    private fun observeConnectivity() {
        viewModelScope.launch {
            var wasOffline = false
            connectivityObserver.isOnline.collect { isOnline ->
                if (isOnline && wasOffline) {
                    refresh()
                }
                wasOffline = !isOnline
            }
        }
    }
}
