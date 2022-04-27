package com.example.frequency.data.local.dao.station

import androidx.room.Dao
import androidx.room.Query

@Dao
interface DeleteStationDao {

    @Query("DELETE FROM station_list WHERE station_uuid =:stationUuid")
    suspend fun deleteStation(stationUuid: String): Int

    @Query("DELETE FROM station_list")
    suspend fun deleteAllStations(): Int

}