package com.example.frequency.presentation.ui.screens.info.profile

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import com.example.frequency.presentation.ui.MainVM.Companion.GAUTH
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.data.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.common.utils.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

const val KEY_USER = "KEY_USER"

@HiltViewModel
class ProfileVM @Inject constructor(
    private val authFirebaseAuth: FirebaseAuth,
    private val shearedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val _showPbLd = MutableLiveEvent<Boolean>()
    val showPbLd = _showPbLd.share()

    private val userFormPref = User(
        shearedPreferences.getUsername(),
        shearedPreferences.getEmail(),
        shearedPreferences.getIconUri(),
        if (shearedPreferences.getRegistrationType() == GAUTH) {
            shearedPreferences.getGToken()
        } else {
            shearedPreferences.getPassword()
        }
    )

    private val _userLD = savedStateHandle.getLiveData(KEY_USER, userFormPref)
    val user: LiveData<User> = _userLD.share()

    fun setUpdatedImage(uri: Uri) {
        shearedPreferences.setIconUri(uri)
        val newUser = user.value?.copy(icon = uri)
        _userLD.value = newUser
    }

}