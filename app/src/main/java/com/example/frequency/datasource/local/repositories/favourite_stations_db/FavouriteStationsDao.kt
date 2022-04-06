package com.example.frequency.datasource.local.repositories.favourite_stations_db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.frequency.datasource.local.repositories.favourite_stations_db.entites.StationEntity

@Dao
interface FavouriteStationsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(vararg stations: StationEntity)

    @Query("SELECT * FROM fav_station_list")
    suspend fun getListOfFavouriteStations(): List<StationEntity?>

    @Query("SELECT * FROM fav_station_list WHERE station_uuid =:stationUuid")
    suspend fun isStationInFavourite(stationUuid: String): StationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addStation(stationEntity: StationEntity)

    @Query("DELETE FROM fav_station_list WHERE station_uuid =:stationUuid")
    suspend fun deleteStation(stationUuid: String): Int

    @Query("DELETE FROM fav_station_list")
    suspend fun deleteAllStations(): Int

}