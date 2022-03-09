package com.example.frequency.screen.settings

import androidx.lifecycle.SavedStateHandle
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    shearedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val _usersEmailLD = savedStateHandle.getLiveData(STATE_KEY_EMAIL, shearedPreferences.getEmail())
    val usersEmailLD = _usersEmailLD.share()

    companion object {
        @JvmStatic
        private val STATE_KEY_EMAIL = "STATE_KEY_EMAIL"
    }
}