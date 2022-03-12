package com.example.frequency.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String,
    val email: String,
    val icon: Uri,
    val secureKey: String
): Parcelable