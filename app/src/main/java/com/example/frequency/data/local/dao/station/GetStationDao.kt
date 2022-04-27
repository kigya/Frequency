package com.example.frequency.data.local.dao.station

import androidx.room.Dao
import androidx.room.Query
import com.example.frequency.data.model.entity.station.StationEntity

@Dao
interface GetStationDao {

    @Query("SELECT * FROM station_list WHERE is_favourite = 1")
    suspend fun getListOfFavouriteStations(): List<StationEntity?>

    @Query("SELECT * FROM station_list WHERE station_uuid =:stationUuid")
    suspend fun isStationInFavourite(stationUuid: String): StationEntity?

    @Query("SELECT * FROM station_list")
    abstract fun getListOfAllStations(): List<StationEntity?>

}