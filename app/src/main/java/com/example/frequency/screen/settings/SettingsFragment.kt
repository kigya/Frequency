package com.example.frequency.screen.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.preference.PreferenceFragmentCompat
import com.example.frequency.R
import com.example.frequency.foundation.contract.ProvidesCustomTitle

class SettingsFragment : PreferenceFragmentCompat(), ProvidesCustomTitle {

    private val viewModel by viewModels<SettingsVM>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {


        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun getTitleRes() = R.string.settings

}