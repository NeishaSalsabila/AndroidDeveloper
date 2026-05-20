package com.neisha.technicaltest_androiddeveloper.data.remote

import com.neisha.technicaltest_androiddeveloper.data.remote.dto.CityDto
import com.neisha.technicaltest_androiddeveloper.data.remote.dto.CreateUserDto
import com.neisha.technicaltest_androiddeveloper.data.remote.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("user")
    suspend fun getUsers(): List<UserDto>

    @POST("user")
    suspend fun createUser(@Body user: CreateUserDto): UserDto

    @GET("city")
    suspend fun getCities(): List<CityDto>
}
