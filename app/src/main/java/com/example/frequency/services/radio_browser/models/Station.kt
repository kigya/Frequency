package com.example.frequency.services.radio_browser.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Station(
    @SerializedName("name") val name: String?,
    @SerializedName("homepage") val homepage: String?,
    @SerializedName("url") val url: String?,
    @SerializedName("urlResolved") val urlResolved: String?,
    @SerializedName("bitrate") val bitrate: String?,
    @SerializedName("languageCodes") val languageCodes: String?,
    @SerializedName("language") val language: String?,
    @SerializedName("votes") val votes: String?,
    @SerializedName("tags") val tags: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("countryCode") val countryCode: String?,
    @SerializedName("clickTrend") val clickTrend: String?,
    @SerializedName("changeUuid") val changeUuid: String?,
    @SerializedName("lastCheckOk") val lastCheckOk: String?,
    @SerializedName("clickCount") val clickCount: String?,
    @SerializedName("favicon") val favicon: String?,
    @SerializedName("hls") val hls: String?,
    @SerializedName("codec") val codec: String?,
    @SerializedName("sslError") val sslError: String?,
    @SerializedName("stationUuid") val stationUuid: String?,
    @SerializedName("lastCheckTime") val lastCheckTime: String?,
    @SerializedName("lastChangeTime") val lastChangeTime: String?,
    @SerializedName("clickTimestamp") val clickTimestamp: String?,
    @SerializedName("lastCheckOkTime") val lastCheckOkTime: String?,
    @SerializedName("lastLocalCheckTime") val lastLocalCheckTime: String?,
    @SerializedName("lastCheckTimeIso8601") val lastCheckTimeIso8601: String?,
    @SerializedName("clickTimestampIso8601") val clickTimestampIso8601: String?,
    @SerializedName("lastChangeTimeIso8601") val lastChangeTimeIso8601: String?,
    @SerializedName("lastCheckOkTimeIso8601") val lastCheckOkTimeIso8601: String?,
    @SerializedName("lastLocalCheckTimeIso8601") val lastLocalCheckTimeIso8601: String?,
    @SerializedName("hasExtendedInfo") val hasExtendedInfo: String?,
) : Parcelable