package com.example.frequency.screen.home

import androidx.lifecycle.*
import com.example.frequency.MainVM.Companion.GAUTH
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.services.radio_browser.radostation_list.NullableStations
import com.example.frequency.services.radio_browser.radostation_list.RadioBrowser
import com.example.frequency.utils.Event
import com.example.frequency.utils.MutableLiveEvent
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val radioBrowser: RadioBrowser,
    private val shearedPreferences: AppDefaultPreferences,
    private val savedStateHandle: SavedStateHandle
) : BaseVM(), LifecycleEventObserver {

    private var currentOffset = 0

    private val _userLD = savedStateHandle.getLiveData<User>(STATE_USER)
    val userLD = _userLD.share()

    private val _stationListLD = savedStateHandle.getLiveData<NullableStations>(STATIONS)
    val stationListLD = _stationListLD.share()

    private val _showPbLd = MutableLiveEvent<Boolean>()
    val showPbLd = _showPbLd.share()

    init {
        updateUser()
    }

    private fun initSSH() {
        if (!savedStateHandle.contains(STATIONS)) {
            savedStateHandle.set(STATIONS, stationListLD.value)
        }
        if (!savedStateHandle.contains(STATE_USER)) {
            savedStateHandle.set(STATE_USER, userLD.value)
        }
    }

    private fun updateUser(user: User? = null) {
        if (user == null) {
            val newUserValue = User(
                shearedPreferences.getUsername(),
                shearedPreferences.getEmail(),
                shearedPreferences.getIconUri(),
                if (shearedPreferences.getRegistrationType() == GAUTH) {
                    shearedPreferences.getGToken()
                } else {
                    shearedPreferences.getPassword()
                }
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


    private fun getStationList() {
        if (stationListLD.value == null) {
            loadStation(reversed = false)//reversed = true
        }
    }

    fun loadStation(offset: Int? = 0, reversed: Boolean = false, direction: Boolean = true) {
        when {
            currentOffset - 25 < 0 -> currentOffset = 0
            !direction -> currentOffset - 25
            direction -> currentOffset + 25
        }

        viewModelScope.launch(Dispatchers.IO) {
            _showPbLd.postValue(Event(true))
            val list =
                radioBrowser.getWideSearchStation(
                    searchRequest = "",
                    offset = currentOffset
                ) ?: emptyList()
            _stationListLD.postValue(list)
            delay(400)
            _showPbLd.postValue(Event(false))
        }
        if (!direction && currentOffset - 25 >= 0) {
            currentOffset -= 25
        } else {
            currentOffset += 25
        }

    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                getStationList()
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
        private val TAG = HomeVM::class.java.simpleName

        @JvmStatic
        private val STATIONS = "STATIONS"

        @JvmStatic
        private val STATE_USER = "STATE_USER"


    }
}