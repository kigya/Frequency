package com.example.frequency.ui.screens.home.home_state_data

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class HomeErrorModel(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val message: Int,
    @StringRes val positive: Int,
    @StringRes val neutral: Int = 0,
    @StringRes val negative: Int = 0
) : Parcelable
