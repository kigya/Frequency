package com.example.frequency.ui.rendering.data

import com.example.frequency.datasource.network.radio_browser.radostation_list.NullableStations

data class StationsUiModel(
    var stations: List<NullableStations> = listOf()
)