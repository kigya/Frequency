package com.example.frequency.presentation.ui.screens.sation_lists.home.model

import android.os.Parcelable
import com.example.frequency.data.remote.NullableStations
import com.example.frequency.data.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeUIModel(
    var user: User,
    var currentOffset: Int,
    var queryLD: String,
    var stationList: NullableStations,
    var tagList: List<String>,
    ) : Parcelable
