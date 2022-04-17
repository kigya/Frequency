package com.example.frequency.ui.screens.sation_lists.home.model

import android.os.Parcelable
import com.example.frequency.datasource.network.radio_browser.radostation_list.NullableStations
import com.example.frequency.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeUIModel(
    var user: User,
    var currentOffset: Int,
    var queryLD: String,
    var stationList: NullableStations,
    var tagList: List<String>,
    ) : Parcelable
