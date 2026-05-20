package com.neisha.technicaltest_androiddeveloper.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey val id: String,
    val name: String,
    val address: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val gender: Int
)

@Entity(tableName = "cities")
data class CityEntity(
    @PrimaryKey val id: String,
    val name: String
)