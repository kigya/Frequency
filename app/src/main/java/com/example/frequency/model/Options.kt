package com.example.frequency.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Options(
    val boxCount: Int,
    val isTimerEnable: Boolean
) : Parcelable {

    companion object {
        @JvmStatic
        val DEFAULT = Options(
            boxCount = 3,
            isTimerEnable = false
        )
    }
}