package com.example.frequency.data.model.utilitar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Options(
    val name: String
) : Parcelable {

    companion object {
        @JvmStatic
        val DEFAULT = Options(
            name = "Volume property"
        )
    }
}