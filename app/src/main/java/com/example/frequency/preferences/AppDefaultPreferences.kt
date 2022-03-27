package com.example.frequency.preferences

import android.net.Uri

interface AppDefaultPreferences {

    fun setRegistrationType(type: Int)

    fun setAutoLoginStatus(status: Boolean)

    fun setNotificationStatus(status: Boolean)

    fun setNotificationVolume(volume: Int)

    fun setLanguage(language: String)

    fun setUsername(username: String)

    fun setEmail(email: String)

    fun setPassword(password: String)

    fun setIconUri(iconUri: Uri)

    fun setGToken(token: String)

    fun getRegistrationType(): Int

    fun getAutologinStatus(): Boolean

    fun getNotificationStatus(): Boolean

    fun getNotificationVolume(): String

    fun getLanguage(): String

    fun getUsername(): String

    fun getEmail(): String

    fun getIconUri(): Uri

    fun getPassword(): String

    fun getGToken(): String

    fun clearAllPreferences()

    fun clearUserPreferences()

    // LISTENERS
    fun removePreferencesListener(listener: PreferencesListener)

    fun addPreferencesListener(listener: PreferencesListener)

    fun clearPreferencesListeners()

}

