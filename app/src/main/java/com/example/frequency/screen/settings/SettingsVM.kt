package com.example.frequency.screen.settings

import androidx.lifecycle.SavedStateHandle
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsVM @Inject constructor(
    private val shearedPreferences: AppDefaultPreferences,
    private val savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val _userLD = savedStateHandle.getLiveData<User>(STATE_USER)
    val userLD = _userLD.share()

    private val _usersEmailLD = savedStateHandle.getLiveData(STATE_KEY_EMAIL, shearedPreferences.getEmail())
    val usersEmailLD = _usersEmailLD.share()

    init {
        updateUser()
    }

    fun updateUser(user: User? = null) {
        if (user == null) {
            val newUserValue = User(
                shearedPreferences.getUsername(),
                shearedPreferences.getEmail(),
                shearedPreferences.getIconUri(),
                shearedPreferences.getGToken(),
            )
            initializeUser(newUserValue)
        } else {
            initializeUser(user)
        }
    }

    private fun initializeUser(user: User) {
        if (_userLD.value != user) {
            _userLD.value = user
            savedStateHandle.set(STATE_USER, user)
        }
    }

    companion object {
        @JvmStatic
        private val STATE_KEY_EMAIL = "STATE_KEY_EMAIL"

        @JvmStatic
        private val STATE_USER = "STATE_USER"
    }
}