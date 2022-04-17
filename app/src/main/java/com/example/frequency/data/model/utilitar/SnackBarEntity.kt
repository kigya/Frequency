package com.example.frequency.data.model.utilitar

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SnackBarEntity(
    val message: Int,
    val additional: String? = null,
    val iconTag: Int? = null
) : Parcelable {

}