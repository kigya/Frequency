package com.example.frequency.datasource.local.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frequency.datasource.local.repositories.room.user_db.entites.UserEntity
import com.example.frequency.datasource.local.repositories.room.user_db.room.UserDao

@Database(
    version = 1,
    entities = [UserEntity::class],
    exportSchema = false // TODO need to set schema
)
abstract class AppDB : RoomDatabase() {

    abstract fun getUserDao(): UserDao

}