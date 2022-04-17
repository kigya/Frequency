package com.example.frequency.foundation.model.state

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class ErrorModel(
    @DrawableRes val icon: Int,
    @StringRes val title: Int,
    @StringRes val message: Int,
    @StringRes val positive: Int,
    @StringRes val neutral: Int = 0,
    @StringRes val negative: Int = 0
) : Parcelable