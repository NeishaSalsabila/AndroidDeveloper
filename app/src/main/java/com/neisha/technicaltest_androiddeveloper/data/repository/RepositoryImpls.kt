package com.neisha.technicaltest_androiddeveloper.data.repository

import com.neisha.technicaltest_androiddeveloper.data.local.dao.CityDao
import com.neisha.technicaltest_androiddeveloper.data.local.dao.UserDao
import com.neisha.technicaltest_androiddeveloper.data.mapper.toCreateDto
import com.neisha.technicaltest_androiddeveloper.data.mapper.toDomain
import com.neisha.technicaltest_androiddeveloper.data.mapper.toEntity
import com.neisha.technicaltest_androiddeveloper.data.remote.ApiService
import com.neisha.technicaltest_androiddeveloper.domain.model.City
import com.neisha.technicaltest_androiddeveloper.domain.model.User
import com.neisha.technicaltest_androiddeveloper.domain.repository.CityRepository
import com.neisha.technicaltest_androiddeveloper.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val userDao: UserDao
) : UserRepository {

    override fun getUsers(): Flow<List<User>> =
        userDao.getAllUsers().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshUsers() {
        try {
            val remote = api.getUsers()
            userDao.deleteAll()
            userDao.insertAll(remote.map { it.toEntity() })
        } catch (e: Exception) {
        }
    }

    override suspend fun addUser(user: User): Result<Unit> {
        return try {
            val response = api.createUser(user.toCreateDto())
            userDao.insert(response.toEntity())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun isEmailTaken(email: String): Boolean =
        userDao.isEmailExists(email)
}

@Singleton
class CityRepositoryImpl @Inject constructor(
    private val api: ApiService,
    private val cityDao: CityDao
) : CityRepository {

    override fun getCities(): Flow<List<City>> =
        cityDao.getAllCities().map { entities -> entities.map { it.toDomain() } }

    override suspend fun refreshCities() {
        try {
            val remote = api.getCities()
            cityDao.deleteAll()
            cityDao.insertAll(remote.map { it.toEntity() })
        } catch (e: Exception) {
        }
    }
}