package com.example.frequency.preferences

import android.net.Uri

interface AppDefaultPreferences {

    fun setAutoLoginStatus(status:Boolean)

    fun setNotificationStatus(status: Boolean)

    fun setNotificationVolume(volume: String)

    fun setLanguage(language: String)

    fun setUsername(username: String)

    fun setEmail(email: String)

    fun setIconUri(iconUri: Uri)

    fun setToken(token: String)

    fun getAutologinStatus(): Boolean

    fun getNotificationStatus(): Boolean

    fun getNotificationVolume(): String

    fun getLanguage(): String

    fun getUsername(): String

    fun getEmail(): String

    fun getIconUri(): Uri

    fun getToken(): String

    fun clearAllPreferences()

    fun clearUserPreferences()

    // LISTENERS
    fun removePreferencesListener(listener: PreferencesListener)

    fun addPreferencesListener(listener: PreferencesListener)

    fun clearPreferencesListeners()

}

