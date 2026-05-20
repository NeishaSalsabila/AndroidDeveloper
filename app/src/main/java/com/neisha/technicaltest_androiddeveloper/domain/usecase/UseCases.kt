package com.neisha.technicaltest_androiddeveloper.domain.usecase

import com.neisha.technicaltest_androiddeveloper.domain.model.City
import com.neisha.technicaltest_androiddeveloper.domain.model.User
import com.neisha.technicaltest_androiddeveloper.domain.repository.CityRepository
import com.neisha.technicaltest_androiddeveloper.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(): Flow<List<User>> = userRepository.getUsers()
}

class SearchUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    operator fun invoke(query: String, cityFilter: String, sortAscending: Boolean): Flow<List<User>> {
        return userRepository.getUsers().map { users ->
            users
                .filter { user ->
                    val matchesQuery = query.isBlank() ||
                            user.name.contains(query, ignoreCase = true) ||
                            user.email.contains(query, ignoreCase = true) ||
                            user.phoneNumber.contains(query, ignoreCase = true)
                    val matchesCity = cityFilter.isBlank() || user.city.equals(cityFilter, ignoreCase = true)
                    matchesQuery && matchesCity
                }
                .let { filtered ->
                    if (sortAscending) filtered.sortedBy { it.name.lowercase() }
                    else filtered.sortedByDescending { it.name.lowercase() }
                }
        }
    }
}

class AddUserUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(user: User): Result<Unit> = userRepository.addUser(user)
}

class RefreshUsersUseCase @Inject constructor(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke() = userRepository.refreshUsers()
}

class GetCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    operator fun invoke(): Flow<List<City>> = cityRepository.getCities()
}

class RefreshCitiesUseCase @Inject constructor(
    private val cityRepository: CityRepository
) {
    suspend operator fun invoke() = cityRepository.refreshCities()
}