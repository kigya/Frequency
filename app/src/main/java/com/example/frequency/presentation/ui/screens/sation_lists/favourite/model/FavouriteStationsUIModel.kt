package com.example.frequency.presentation.ui.screens.sation_lists.favourite.model

import android.os.Parcelable
import com.example.frequency.data.remote.NullableStations
import com.example.frequency.data.model.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class FavouriteStationsUIModel(
    var user: User,
    var stationList: NullableStations,
    ) : Parcelable
