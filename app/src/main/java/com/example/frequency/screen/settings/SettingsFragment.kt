package com.example.frequency.screen.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.frequency.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

}