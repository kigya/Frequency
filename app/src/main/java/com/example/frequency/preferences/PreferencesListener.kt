package com.example.frequency.preferences

import android.content.SharedPreferences

interface PreferencesListener {
    fun onPreferencesUpdated(preferences: SharedPreferences, key: String)
}