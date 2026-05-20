package com.neisha.technicaltest_androiddeveloper.data.mapper

import com.neisha.technicaltest_androiddeveloper.data.local.entity.CityEntity
import com.neisha.technicaltest_androiddeveloper.data.local.entity.UserEntity
import com.neisha.technicaltest_androiddeveloper.data.remote.dto.CityDto
import com.neisha.technicaltest_androiddeveloper.data.remote.dto.CreateUserDto
import com.neisha.technicaltest_androiddeveloper.data.remote.dto.UserDto
import com.neisha.technicaltest_androiddeveloper.domain.model.City
import com.neisha.technicaltest_androiddeveloper.domain.model.User

fun UserDto.toEntity() = UserEntity(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = gender
)

fun UserEntity.toDomain() = User(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = gender
)

fun User.toCreateDto() = CreateUserDto(
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = gender
)

fun User.toEntity() = UserEntity(
    id = id,
    name = name,
    address = address,
    email = email,
    phoneNumber = phoneNumber,
    city = city,
    gender = gender
)

fun CityDto.toEntity() = CityEntity(id = id, name = name)

fun CityEntity.toDomain() = City(id = id, name = name)
