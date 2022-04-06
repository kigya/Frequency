package com.example.frequency.datasource.local.repositories.favourite_stations_db

import com.example.frequency.datasource.network.radio_browser.models.Station
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FavouriteStationsRepositoryImpl @Inject constructor(
    private val favouriteStationsDao: FavouriteStationsDao
) : FavouriteStationsRepository {

    override suspend fun insertAll(vararg stations: Station) {
        return withContext(Dispatchers.IO) {
            return@withContext favouriteStationsDao.insertAll(*stations.map { it.toStationEntity() }.toTypedArray())
        }
    }

    override suspend fun getListOfFavouriteStations(): List<Station?> {
        return withContext(Dispatchers.IO) {
            return@withContext favouriteStationsDao.getListOfFavouriteStations()
                .map { it?.toStation() }.toList()
        }
    }

    override suspend fun isStationInFavourite(stationUuid: String): Station? {
        return withContext(Dispatchers.IO) {
            return@withContext favouriteStationsDao.isStationInFavourite(stationUuid)?.toStation()
        }
    }

    override suspend fun addStation(entity: Station) {
        return favouriteStationsDao.addStation(entity.toStationEntity())
    }

    override suspend fun deleteStation(stationUuid: String): Int {
        return favouriteStationsDao.deleteStation(stationUuid)
    }

    override suspend fun deleteAllStations(): Int {
        return favouriteStationsDao.deleteAllStations()
    }
}