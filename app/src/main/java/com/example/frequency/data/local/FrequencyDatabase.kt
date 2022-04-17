package com.example.frequency.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frequency.data.local.dao.station.FavouriteStationsDao
import com.example.frequency.data.model.entity.station.StationEntity
import com.example.frequency.data.local.dao.user.UserDao
import com.example.frequency.data.model.entity.user.UserEntity

@Database(
    version = 1,
    entities = [UserEntity::class, StationEntity::class],
    exportSchema = false
)
abstract class FrequencyDatabase : RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getFavouriteStationsDao(): FavouriteStationsDao

}