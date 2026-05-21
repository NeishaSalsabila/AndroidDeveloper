package com.neisha.technicaltest_androiddeveloper.domain.model

data class User(
    val id: String,
    val name: String,
    val address: String,
    val email: String,
    val phoneNumber: String,
    val city: String,
    val gender: Int
)

data class City(
    val id: String,
    val name: String
)

enum class SortOption {
    NAME_ASC,
    NAME_DESC
}