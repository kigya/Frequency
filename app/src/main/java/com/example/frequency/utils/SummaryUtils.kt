package com.example.frequency.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.frequency.R
import com.example.frequency.model.actions.MenuAction

fun <T> MutableLiveData<T>.share(): LiveData<T> = this

object SettingTags {
    const val AUTOLOGIN = "AUTOLOGIN"
    const val LANGUAGE = "LANGUAGE"
    const val NOTIFICATION_VOLUME = "NOTIFICATION_VOLUME"
    const val DISABLE_NOTIFICATIONS = "DISABLE_NOTIFICATIONS"
    const val USERNAME = "USERNAME"
    const val EMAIL = "EMAIL"
    const val ICON_URI = "ICON_URI"
    const val TOKEN = "TOKEN"
}

val menuAction = MenuAction(
    iconRes = R.drawable.ic_navigation_menu,
    textRes = R.string.menu,
    null
)