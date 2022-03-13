package com.example.frequency.screen.song

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.SavedStateHandle
import com.example.frequency.MainVM
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.services.radio_browser.models.Station
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SongVM @Inject constructor(
    private val sharedPreferences: AppDefaultPreferences,
    private val savedStateHandle: SavedStateHandle,
) : BaseVM(), LifecycleEventObserver {

    private val _userLD =
        savedStateHandle.getLiveData<User>(STATE_USER)
    val userLD = _userLD.share()

    private val _stationLD = savedStateHandle.getLiveData<Station>(STATE_STATION)
    val stationLD = _stationLD.share()


    init {
        updateUser()
    }

    fun updateStation(station: Station? = null) {
        if (station != null) {
            initializeStation(station)
        }
    }
    private fun initializeStation(station: Station){
        if (stationLD.value != station) {
            _stationLD.value = station
            savedStateHandle.set(STATE_STATION, station)
        }
    }

    private fun updateUser(user: User? = null) {
        if (user == null) {
            val newUserValue = User(
                sharedPreferences.getUsername(),
                sharedPreferences.getEmail(),
                sharedPreferences.getIconUri(),
                if (sharedPreferences.getRegistrationType() == MainVM.GAUTH) {
                    sharedPreferences.getGToken()
                } else {
                    sharedPreferences.getPassword()
                }
            )
            initializeUser(newUserValue)
        } else {
            initializeUser(user)
        }
    }

    private fun initializeUser(user: User) {
        if (userLD.value != user) {
            _userLD.value = user
            savedStateHandle.set(STATE_USER, user)
        }
    }

    private fun initSSH() {
        if (!savedStateHandle.contains(STATE_STATION)) {
            savedStateHandle.set(STATE_STATION, stationLD.value)
        }
        if (!savedStateHandle.contains(STATE_USER)) {
            savedStateHandle.set(STATE_USER, userLD.value)
        }
    }


    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
            }
            Lifecycle.Event.ON_START -> {}
            Lifecycle.Event.ON_RESUME -> {}
            Lifecycle.Event.ON_PAUSE -> {}
            Lifecycle.Event.ON_STOP -> {}
            Lifecycle.Event.ON_DESTROY -> {
                initSSH()
            }
            Lifecycle.Event.ON_ANY -> {}
        }
    }

    companion object {
        @JvmStatic
        private val STATE_STATION = "STATE_STATION"

        @JvmStatic
        private val STATE_USER = "STATE_USER"
    }
}