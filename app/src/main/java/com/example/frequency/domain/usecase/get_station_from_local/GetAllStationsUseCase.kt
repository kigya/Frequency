package com.example.frequency.domain.usecase.get_station_from_local

import com.example.frequency.domain.repository.local.station_repository.StationsRepository
import javax.inject.Inject

class GetAllStationsUseCase @Inject constructor(
    private val repository: StationsRepository
) {
    suspend operator fun invoke() = repository.getListOfAllStations()
}