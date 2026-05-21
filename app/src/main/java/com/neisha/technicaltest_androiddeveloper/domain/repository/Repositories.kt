package com.neisha.technicaltest_androiddeveloper.domain.repository

import com.neisha.technicaltest_androiddeveloper.domain.model.City
import com.neisha.technicaltest_androiddeveloper.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getUsers(): Flow<List<User>>
    suspend fun refreshUsers()
    suspend fun addUser(user: User): Result<Unit>
    suspend fun isEmailTaken(email: String): Boolean
}

interface CityRepository {
    fun getCities(): Flow<List<City>>
    suspend fun refreshCities()
}
