package com.neisha.technicaltest_androiddeveloper.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.neisha.technicaltest_androiddeveloper.analytics.AnalyticsHelper
import com.neisha.technicaltest_androiddeveloper.domain.model.City
import com.neisha.technicaltest_androiddeveloper.domain.model.User
import com.neisha.technicaltest_androiddeveloper.domain.usecase.AddUserUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.GetCitiesUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.RefreshCitiesUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.RefreshUsersUseCase
import com.neisha.technicaltest_androiddeveloper.domain.usecase.SearchUsersUseCase
import com.neisha.technicaltest_androiddeveloper.worker.SyncWorker
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
    val selectedCity: String = "",
    val sortAscending: Boolean = true
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
    private val refreshUsersUseCase: RefreshUsersUseCase,
    private val getCitiesUseCase: GetCitiesUseCase,
    private val refreshCitiesUseCase: RefreshCitiesUseCase,
    private val analyticsHelper: AnalyticsHelper,
    private val workManager: WorkManager
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    private val _selectedCity = MutableStateFlow("")
    private val _sortAscending = MutableStateFlow(true)
    private val _isLoading = MutableStateFlow(false)
    private val _isRefreshing = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)
    private val _addUserState = MutableStateFlow<AddUserUiState>(AddUserUiState.Idle)

    val addUserState: StateFlow<AddUserUiState> = _addUserState.asStateFlow()

    // Debounced search + filter + sort → reactive Flow pipeline
    val uiState: StateFlow<UserListUiState> = combine(
        _searchQuery.debounce(300),
        _selectedCity,
        _sortAscending,
        _isLoading,
        _isRefreshing,
        _error,
        getCitiesUseCase()
    ) { values ->
        val query = values[0] as String
        val city = values[1] as String
        val sort = values[2] as Boolean
        val loading = values[3] as Boolean
        val refreshing = values[4] as Boolean
        val error = values[5] as String?
        @Suppress("UNCHECKED_CAST")
        val cities = values[6] as List<City>
        Triple(Triple(query, city, sort), Triple(loading, refreshing, error), cities)
    }.flatMapLatest { (filters, status, cities) ->
        val (query, city, sort) = filters
        val (loading, refreshing, error) = status
        searchUsersUseCase(query, city, sort).combine(
            kotlinx.coroutines.flow.flowOf(cities)
        ) { users, cityList ->
            UserListUiState(
                users = users,
                cities = cityList,
                isLoading = loading,
                isRefreshing = refreshing,
                error = error,
                searchQuery = query,
                selectedCity = city,
                sortAscending = sort
            )
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UserListUiState(isLoading = true)
    )

    init {
        scheduleSyncWork()
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
        _selectedCity.update { city }
        if (city.isNotBlank()) analyticsHelper.logFilterApplied(city)
    }

    fun onSortToggle() {
        _sortAscending.update { !it }
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
                _addUserState.update { AddUserUiState.Success }
            } else {
                _addUserState.update {
                    AddUserUiState.Error(result.exceptionOrNull()?.message ?: "Gagal menambahkan user.")
                }
            }
        }
    }

    private fun scheduleSyncWork() {
        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()
        workManager.enqueue(request)
    }
}
