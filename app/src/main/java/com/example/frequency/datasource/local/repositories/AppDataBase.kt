package com.example.frequency.datasource.local.repositories

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frequency.datasource.local.repositories.favourite_stations_db.FavouriteStationsDao
import com.example.frequency.datasource.local.repositories.favourite_stations_db.entites.StationEntity
import com.example.frequency.datasource.local.repositories.user_db.entites.UserEntity
import com.example.frequency.datasource.local.repositories.user_db.room.UserDao

@Database(
    version = 1,
    entities = [UserEntity::class, StationEntity::class],
    exportSchema = false
)
abstract class AppDataBase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getFavouriteStationsDao(): FavouriteStationsDao

}