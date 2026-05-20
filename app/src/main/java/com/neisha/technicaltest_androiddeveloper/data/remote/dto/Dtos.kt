package com.neisha.technicaltest_androiddeveloper.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class UserDto(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String = "",
    @Json(name = "address") val address: String = "",
    @Json(name = "email") val email: String = "",
    @Json(name = "phoneNumber") val phoneNumber: String = "",
    @Json(name = "city") val city: String = "",
    @Json(name = "gender") val gender: Int = 0
)

@JsonClass(generateAdapter = true)
data class CreateUserDto(
    @Json(name = "name") val name: String,
    @Json(name = "address") val address: String,
    @Json(name = "email") val email: String,
    @Json(name = "phoneNumber") val phoneNumber: String,
    @Json(name = "city") val city: String,
    @Json(name = "gender") val gender: Int
)

@JsonClass(generateAdapter = true)
data class CityDto(
    @Json(name = "id") val id: String = "",
    @Json(name = "name") val name: String = ""
)

