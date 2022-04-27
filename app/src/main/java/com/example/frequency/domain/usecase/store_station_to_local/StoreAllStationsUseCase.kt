package com.example.frequency.domain.usecase.store_station_to_local

import com.example.frequency.data.model.network.station.Station
import com.example.frequency.domain.repository.local.station_repository.StationsRepository
import javax.inject.Inject

class StoreAllStationsUseCase @Inject constructor(
    private val repository: StationsRepository
) {
    suspend operator fun invoke( stations: List<Station>){
        repository.insertAll(stations)
    }
}