package com.example.frequency.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.frequency.data.local.dao.station.DeleteStationDao
import com.example.frequency.data.local.dao.station.GetStationDao
import com.example.frequency.data.local.dao.station.StoreStationsDao
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

    abstract fun getStoreStationsDao(): StoreStationsDao
    abstract fun getGetStationsDao(): GetStationDao
    abstract fun getDeleteStationsDao(): DeleteStationDao

}