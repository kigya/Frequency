package com.example.frequency.repositories.remote.radio_browser.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RadioStationsListResultsHeaders(
    val accessControlAllowCredentials: String,
    val accessControlAllowHeaders: String,
    val accessControlAllowMethods: String,
    val accessControlAllowOrigin: String,
    val connection: String,
    val contentLength: String,
    val contentType: String,
    val date: String,
    val server: String,
    val vary: String,
    val xRapidApiRegion: String,
    val xRapidApiVersion: String
): Parcelable