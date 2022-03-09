package com.example.frequency

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    shearedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    // TODO should refactor this field to instance of user
    private val _userEmailLD =
        savedStateHandle.getLiveData(STATE_KEY_EMAIL, shearedPreferences.getEmail())
    val userEmailLD = _userEmailLD.share()

    private val _userIconLD =
        savedStateHandle.getLiveData(STATE_KEY_ICON, shearedPreferences.getIconUri())
    val userIconLD = _userIconLD.share()

    private val _userNameLD =
        savedStateHandle.getLiveData(STATE_KEY_NAME, shearedPreferences.getUsername())
    val userNameLD = _userNameLD.share()

    private val _autologinLD = MutableLiveData(shearedPreferences.getAutologinStatus())
    val autologinLD = _autologinLD.share()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        viewModelScope.launch {
            delay(1500)
            _isLoading.value = false
        }
        savedStateHandle.set(STATE_KEY_EMAIL, shearedPreferences.getEmail())

    }

    companion object {

        //user
        @JvmStatic
        private val STATE_KEY_NAME = "STATE_KEY_NAME"

        @JvmStatic
        private val STATE_KEY_EMAIL = "STATE_KEY_EMAIL"

        @JvmStatic
        private val STATE_KEY_ICON = "STATE_KEY_ICON"
    }

}