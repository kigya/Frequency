package com.example.frequency.domain.repository.local.station_repository

import com.example.frequency.data.model.network.station.Station
import com.example.frequency.data.remote.NullableStations

interface StationsRepository {

    suspend fun getListOfAllStations(): NullableStations

    suspend fun getListOfFavoriteStations(): List<Station?>

    suspend fun isStationInFavourite(stationUuid: String): Station?

    suspend fun insertAll(stations: List<Station>)

    suspend fun addFavoriteStation(entity: Station)

    suspend fun deleteStation(stationUuid: String): Int

    suspend fun deleteAllStations(): Int

}