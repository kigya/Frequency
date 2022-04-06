package com.example.frequency.datasource.local.repositories.favourite_stations_db

import com.example.frequency.datasource.network.radio_browser.models.Station
import com.example.frequency.datasource.network.radio_browser.radostation_list.NullableStations

interface FavouriteStationsRepository {

    suspend fun insertAll(vararg stations: Station)

    suspend fun getListOfFavouriteStations(): NullableStations

    suspend fun isStationInFavourite(stationUuid: String): Station?

    suspend fun addStation(entity: Station)

    suspend fun deleteStation(stationUuid: String): Int

    suspend fun deleteAllStations(): Int

}