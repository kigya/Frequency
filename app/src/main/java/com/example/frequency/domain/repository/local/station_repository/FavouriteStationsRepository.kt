package com.example.frequency.domain.repository.local.station_repository

import com.example.frequency.data.model.network.station.Station
import com.example.frequency.data.remote.NullableStations

interface FavouriteStationsRepository {

    suspend fun insertAll(vararg stations: Station)

    suspend fun getListOfFavouriteStations(): NullableStations

    suspend fun isStationInFavourite(stationUuid: String): Station?

    suspend fun addStation(entity: Station)

    suspend fun deleteStation(stationUuid: String): Int

    suspend fun deleteAllStations(): Int

}