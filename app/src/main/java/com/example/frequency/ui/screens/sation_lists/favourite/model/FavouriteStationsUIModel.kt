package com.example.frequency.ui.screens.sation_lists.favourite.model

import android.os.Parcelable
import com.example.frequency.datasource.network.radio_browser.radostation_list.NullableStations
import com.example.frequency.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavouriteStationsUIModel(
    var user: User,
    var stationList: NullableStations,
    ) : Parcelable
