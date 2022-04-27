package com.example.frequency.data.repository.local.station

import com.example.frequency.data.local.dao.station.DeleteStationDao
import com.example.frequency.data.local.dao.station.GetStationDao
import com.example.frequency.data.local.dao.station.StoreStationsDao
import com.example.frequency.data.model.network.station.Station
import com.example.frequency.domain.repository.local.station_repository.StationsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class StationsRepositoryImpl @Inject constructor(
    private val storeStationsDao: StoreStationsDao,
    private val getStationDao: GetStationDao,
    private val deleteStationDao: DeleteStationDao,
) : StationsRepository {

    override suspend fun getListOfAllStations(): List<Station?> {
        return withContext(Dispatchers.IO) {
            return@withContext getStationDao.getListOfAllStations()
                .map { it?.toStation() }.toList()
        }
    }

    override suspend fun getListOfFavoriteStations(): List<Station?> {
        return withContext(Dispatchers.IO) {
            return@withContext getStationDao.getListOfFavouriteStations()
                .map { it?.toStation() }.toList()
        }
    }

    override suspend fun isStationInFavourite(stationUuid: String): Station? {
        return withContext(Dispatchers.IO) {
            return@withContext getStationDao.isStationInFavourite(stationUuid)?.toStation()
        }
    }

    override suspend fun insertAll(stations: List<Station>) {
        return withContext(Dispatchers.IO) {
            return@withContext storeStationsDao.insertAll(stations.map { it.toStationEntity() })
        }
    }

    override suspend fun addFavoriteStation(entity: Station) {
        return withContext(Dispatchers.IO) {
            return@withContext storeStationsDao.addStation(entity.toStationEntity())
        }
    }

    override suspend fun deleteStation(stationUuid: String): Int {
        return withContext(Dispatchers.IO) {
            return@withContext deleteStationDao.deleteStation(stationUuid)
        }
    }

    override suspend fun deleteAllStations(): Int {
        return withContext(Dispatchers.IO) {
            return@withContext deleteStationDao.deleteAllStations()
        }
    }
}