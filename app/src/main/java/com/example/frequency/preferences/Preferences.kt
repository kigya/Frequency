package com.example.frequency.preferences

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.preference.PreferenceManager
import com.example.frequency.utils.SettingTags.AUTOLOGIN
import com.example.frequency.utils.SettingTags.DISABLE_NOTIFICATIONS
import com.example.frequency.utils.SettingTags.EMAIL
import com.example.frequency.utils.SettingTags.ICON_URI
import com.example.frequency.utils.SettingTags.LANGUAGE
import com.example.frequency.utils.SettingTags.NOTIFICATION_VOLUME
import com.example.frequency.utils.SettingTags.TOKEN
import com.example.frequency.utils.SettingTags.USERNAME
import javax.inject.Inject

class Preferences @Inject constructor() : AppDefaultPreferences {

    companion object {

        private var rootPreferences: SharedPreferences? = null
        private var instance: AppDefaultPreferences? = null

        fun getDefaultPreferenceInstance(context: Context): AppDefaultPreferences {
            if (instance == null) {
                instance = Preferences()
                rootPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            }
            return instance!!
        }
    }

    override fun setAutoLoginStatus(status: Boolean) {
        rootPreferences?.edit()?.putBoolean(AUTOLOGIN, status)?.apply() ?: Unit
    }

    override fun setNotificationStatus(status: Boolean) {
        rootPreferences?.edit()?.putBoolean(DISABLE_NOTIFICATIONS, status)?.apply() ?: Unit
    }

    override fun setNotificationVolume(volume: String) {
        rootPreferences?.edit()?.putString(NOTIFICATION_VOLUME, volume)?.apply() ?: Unit
    }

    override fun setLanguage(language: String) {
        rootPreferences?.edit()?.putString(LANGUAGE, language)?.apply() ?: Unit
    }

    override fun setUsername(username: String) {
        rootPreferences?.edit()?.putString(USERNAME, username)?.apply() ?: Unit
    }

    override fun setEmail(email: String) {
        rootPreferences?.edit()?.putString(EMAIL, email)?.apply() ?: Unit
    }

    override fun setIconUri(iconUri: Uri) {
        rootPreferences?.edit()?.putString(ICON_URI, iconUri.toString())?.apply() ?: Unit
    }

    override fun setToken(token: String) {
        rootPreferences?.edit()?.putString(TOKEN, token)?.apply() ?: Unit
    }

    override fun getAutologinStatus(): Boolean =
        rootPreferences?.getBoolean(AUTOLOGIN, true) ?: true

    override fun getNotificationStatus(): Boolean = rootPreferences?.getBoolean(
        DISABLE_NOTIFICATIONS, false
    ) ?: false

    override fun getNotificationVolume(): String =
        rootPreferences?.getString(NOTIFICATION_VOLUME, "50") ?: "50"

    override fun getLanguage(): String = rootPreferences?.getString(LANGUAGE, "en") ?: "en"

    override fun getUsername(): String = rootPreferences?.getString(USERNAME, "") ?: ""

    override fun getEmail(): String = rootPreferences?.getString(EMAIL, "") ?: ""

    override fun getIconUri(): Uri {
        return Uri.parse(rootPreferences?.getString(ICON_URI, "")) ?: Uri.EMPTY
    }

    override fun getToken(): String = rootPreferences?.getString(TOKEN, "") ?: ""

    override fun clearAllPreferences() {
        rootPreferences?.edit()?.putBoolean(AUTOLOGIN, true)?.apply() ?: Unit
        rootPreferences?.edit()?.putBoolean(DISABLE_NOTIFICATIONS, false)?.apply() ?: Unit
        rootPreferences?.edit()?.putString(LANGUAGE, "en")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(NOTIFICATION_VOLUME, "50")?.apply() ?: Unit
        // User
        rootPreferences?.edit()?.putString(USERNAME, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(EMAIL, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(ICON_URI, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(TOKEN, "")?.apply() ?: Unit
    }

    override fun clearUserPreferences() {
        rootPreferences?.edit()?.putString(USERNAME, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(EMAIL, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(ICON_URI, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(TOKEN, "")?.apply() ?: Unit
    }

}