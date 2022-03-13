package com.example.frequency.repositorys.room.app_database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frequency.repositorys.room.user_db.entites.UserEntity
import com.example.frequency.repositorys.room.user_db.room.UserDao

@Database(
    version = 1,
    entities = [UserEntity::class]
)
abstract class AppDB: RoomDatabase() {

    abstract fun getUserDao(): UserDao

}