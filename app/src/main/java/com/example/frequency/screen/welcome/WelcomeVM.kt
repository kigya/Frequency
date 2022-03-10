package com.example.frequency.screen.welcome

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeVM @Inject constructor(
    private val sharedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val _registerUserLD = savedStateHandle.getLiveData<User>(STATE_KEY_USER)
    val registerUserLD = _registerUserLD.share()

    fun registerUser(
        name: String,
        email: String,
        icon: Uri,
        gToken: String
    ) {
        _registerUserLD.value = User(name, email, icon, gToken)
    }

    fun addUserToShearedPrefs(
        name: String,
        email: String,
        icon: Uri,
        gToken: String
    ) {
        sharedPreferences.setUsername(name)
        sharedPreferences.setEmail(email)
        sharedPreferences.setIconUri(icon)
        sharedPreferences.setToken(gToken)
    }

    companion object {
        @JvmStatic
        private val STATE_KEY_USER = "STATE_KEY_USER"
    }

}