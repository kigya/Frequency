package com.example.frequency.screen.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val KEY_USER = "KEY_USER"

@HiltViewModel
class ProfileVM @Inject constructor(
    private val shearedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val userFormPref = User(
        shearedPreferences.getUsername(),
        shearedPreferences.getEmail(),
        shearedPreferences.getIconUri(),
        shearedPreferences.getToken(),
        )

    private val _userLD = savedStateHandle.getLiveData(KEY_USER, userFormPref)
    val user: LiveData<User> = _userLD.share()

    fun clearUserRootPreferences() {
        shearedPreferences.clearAllPreferences()
    }

}