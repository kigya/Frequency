package com.example.frequency.repositorys.room.app_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frequency.repositorys.room.UserDao
import com.example.frequency.repositorys.room.entites.UserEntity

@Database(
    version = 1,
    entities = [UserEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getCrimesDao(): UserDao

}