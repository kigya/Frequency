package com.example.frequency.ui.screens.home

import android.util.Log
import androidx.lifecycle.*
import com.example.frequency.MainVM.Companion.GAUTH
import com.example.frequency.datasource.network.CoroutineDispatcherProvider
import com.example.frequency.datasource.network.radio_browser.radostation_list.NullableStations
import com.example.frequency.datasource.network.radio_browser.radostation_list.RadioBrowser
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.Event
import com.example.frequency.utils.MutableLiveEvent
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(
    private val radioBrowser: RadioBrowser,
    private val shearedPreferences: AppDefaultPreferences,
    private val savedStateHandle: SavedStateHandle,
    private val coroutineDispatcher: CoroutineDispatcherProvider
) : BaseVM(), LifecycleEventObserver {

    private var currentOffset = 0
    private var currentTag = ""

    private val _userLD = savedStateHandle.getLiveData<User>(STATE_USER)
    val userLD = _userLD.share()

    private val _queryLD = savedStateHandle.getLiveData<String>(STATE_QUERY)
    val queryLD = _queryLD.share()

    private val _stationListLD = savedStateHandle.getLiveData<NullableStations>(STATIONS)
    val stationListLD = _stationListLD.share()

    private val _tagListLD = savedStateHandle.getLiveData<List<String>>(TAGS)
    val tagListLD = _tagListLD.share()

    private val _showPbLd = MutableLiveEvent<Boolean>()
    val showPbLd = _showPbLd.share()

    init {
        updateUser()
        _tagListLD.value = tagsList
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
            loadStation()
        }
    }

    fun loadStation() {
        Log.d(TAG, "offset $currentOffset")
        viewModelScope.launch(coroutineDispatcher.IO()) {
            try {
                _showPbLd.postValue(Event(true))
                val list = radioBrowser.getWideSearchStation(
                    searchRequest = queryLD.value ?: "",
                    offset = currentOffset,
                    tag = currentTag,
                ) ?: emptyList()
                _stationListLD.postValue(list)
                delay(400)
            }catch (ex: HttpException){
                Log.e(TAG, ex.message.toString())
                loadStation()
            }catch (ex: Exception){
                Log.e(TAG, ex.message.toString())
            }
            finally {
                _showPbLd.postValue(Event(false))
            }
        }
    }

    fun riseOffset() {
        currentOffset += 25
    }

    fun decreaseOffset() {
        if (currentOffset - 25 <= 0) {
            dropOffset()
        } else {
            currentOffset -= 25
        }
    }

    fun dropOffset() {
        currentOffset = 0
    }

    fun changeTag(tag: String) {
        currentTag = tag
    }

    fun setUpdatedQuery(query: String?) {
        if (queryLD.value != query) {
            _queryLD.value = query
        }
    }

    private fun initSSH() {
        if (!savedStateHandle.contains(STATIONS)) {
            savedStateHandle.set(STATIONS, stationListLD.value)
        }
        if (!savedStateHandle.contains(STATE_USER)) {
            savedStateHandle.set(STATE_USER, userLD.value)
        }
        if (!savedStateHandle.contains(STATE_QUERY)) {
            savedStateHandle.set(STATE_QUERY, queryLD.value)
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                getStationList()
            }

            Lifecycle.Event.ON_DESTROY -> {
                initSSH()
            }
            else -> {}
        }
    }


    companion object {
        @JvmStatic
        private val TAG = HomeVM::class.java.simpleName

        @JvmStatic
        private val STATIONS = "STATIONS"

        @JvmStatic
        private val STATE_USER = "STATE_USER"

        @JvmStatic
        private val STATE_QUERY = "STATE_QUERY"

        @JvmStatic
        private val TAGS = "TAGS"

        @JvmStatic
        private val tagsList = listOf(
            "rock",
            "pop",
            "metal",
            "jazz",
            "country",
            "classical",
            "drum & bass",
            "hip-hop",
            "rnb",
        )
    }
}