package com.example.frequency.repositorys.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frequency.repositorys.room.entites.UserEntity

@Database(
    version = 1,
    entities = [UserEntity::class]
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

}