package com.neisha.technicaltest_androiddeveloper.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.neisha.technicaltest_androiddeveloper.data.local.dao.CityDao
import com.neisha.technicaltest_androiddeveloper.data.local.dao.UserDao
import com.neisha.technicaltest_androiddeveloper.data.local.entity.CityEntity
import com.neisha.technicaltest_androiddeveloper.data.local.entity.UserEntity

@Database(
    entities = [UserEntity::class, CityEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun cityDao(): CityDao
}
