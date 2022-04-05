package com.example.frequency.preferences

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import com.example.frequency.utils.PreferenceTags.AUTOLOGIN
import com.example.frequency.utils.PreferenceTags.DISABLE_NOTIFICATIONS
import com.example.frequency.utils.PreferenceTags.EMAIL
import com.example.frequency.utils.PreferenceTags.ICON_URI
import com.example.frequency.utils.PreferenceTags.LANGUAGE
import com.example.frequency.utils.PreferenceTags.NOTIFICATION_VOLUME
import com.example.frequency.utils.PreferenceTags.PASS
import com.example.frequency.utils.PreferenceTags.REG_TYPE
import com.example.frequency.utils.PreferenceTags.TOKEN
import com.example.frequency.utils.PreferenceTags.USERNAME
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class Preferences @Inject constructor() : AppDefaultPreferences {

    companion object {

        private var rootPreferences: SharedPreferences? = null
        private var instance: AppDefaultPreferences? = null

        private val preferencesListeners = mutableSetOf<PreferencesListener>()

        private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
                if (preferencesListeners.isNotEmpty()) {
                    preferencesListeners.forEach { listener ->
                        listener.onPreferencesUpdated(prefs, key)
                    }
                }
            }

        fun getDefaultPreferenceInstance(context: Context): AppDefaultPreferences {
            if (instance == null) {
                instance = Preferences()
                rootPreferences = PreferenceManager.getDefaultSharedPreferences(context)

                rootPreferences?.registerOnSharedPreferenceChangeListener(prefListener)
            }
            return instance!!
        }
    }

    /*
    private val rootPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

    private val preferencesListeners = mutableSetOf<PreferencesListener>()

    private val prefListener = SharedPreferences.OnSharedPreferenceChangeListener { prefs, key ->
        if (preferencesListeners.isNotEmpty()) {
            preferencesListeners.forEach { listener ->
                listener.onPreferencesUpdated(prefs, key)
            }
        }
    }

    init {
        rootPreferences.registerOnSharedPreferenceChangeListener(prefListener)
    }

    val regType by rootPreferences!!.int(key = { REG_TYPE }, defValue = 1000)
    val autologin by rootPreferences!!.boolean(key = { AUTOLOGIN }, defValue = true)
    val notStatus by rootPreferences!!.boolean(key = { DISABLE_NOTIFICATIONS }, defValue = false)
    val notVolume by rootPreferences!!.int(key = { NOTIFICATION_VOLUME }, defValue = 50)
    val lang by rootPreferences!!.string(key = { LANGUAGE }, defValue = "en")
    val name by rootPreferences!!.string(key = { USERNAME }, defValue = "")
    val mail by rootPreferences!!.string(key = { EMAIL }, defValue = "")
    val pass by rootPreferences!!.string(key = { PASS }, defValue = "")
    val iconUri by rootPreferences!!.string(key = { ICON_URI }, defValue = "")
    val token by rootPreferences!!.string(key = { TOKEN }, defValue = "")*/


    override fun setRegistrationType(type: Int) {
        rootPreferences?.edit()?.putInt(REG_TYPE, type)?.apply() ?: Unit
    }

    override fun setAutoLoginStatus(status: Boolean) {
        rootPreferences?.edit()?.putBoolean(AUTOLOGIN, status)?.apply() ?: Unit
    }

    override fun setNotificationStatus(status: Boolean) {
        rootPreferences?.edit()?.putBoolean(DISABLE_NOTIFICATIONS, status)?.apply() ?: Unit
    }

    override fun setNotificationVolume(volume: Int) {
        rootPreferences?.edit()?.putInt(NOTIFICATION_VOLUME, volume)?.apply() ?: Unit
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

    override fun setPassword(password: String) {
        rootPreferences?.edit()?.putString(PASS, password)?.apply() ?: Unit
    }

    override fun setIconUri(iconUri: Uri) {
        rootPreferences?.edit()?.putString(ICON_URI, iconUri.toString())?.apply() ?: Unit
    }

    override fun setGToken(token: String) {
        rootPreferences?.edit()?.putString(TOKEN, token)?.apply() ?: Unit

    }

    override fun getRegistrationType(): Int =
        rootPreferences?.getInt(REG_TYPE, 1000) ?: 1000

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

    override fun getPassword(): String = rootPreferences?.getString(PASS, "") ?: ""

    override fun getIconUri(): Uri {
        return Uri.parse(rootPreferences?.getString(ICON_URI, "")) ?: Uri.EMPTY
    }

    override fun getGToken(): String = rootPreferences?.getString(TOKEN, "") ?: ""

    override fun clearAllPreferences() {
        rootPreferences?.edit { clear() }
    }

    override fun clearUserPreferences() {
        rootPreferences?.edit()?.putString(USERNAME, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(EMAIL, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(ICON_URI, "")?.apply() ?: Unit
        rootPreferences?.edit()?.putString(TOKEN, "")?.apply() ?: Unit
    }

    override fun addPreferencesListener(listener: PreferencesListener) {
        preferencesListeners.add(listener)
    }

    override fun removePreferencesListener(listener: PreferencesListener) {
        preferencesListeners.remove(listener)
    }

    override fun clearPreferencesListeners() {
        preferencesListeners.clear()
    }

}