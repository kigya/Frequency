package com.example.frequency.domain.usecase.delete_station_from_local

import com.example.frequency.domain.repository.local.station_repository.StationsRepository
import javax.inject.Inject

class DeleteStationFromLocalUseCase @Inject constructor(
    private val repository: StationsRepository
) {
    suspend operator fun invoke(stationUuid:String) =
        repository.deleteStation(stationUuid)

}