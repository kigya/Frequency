package com.example.frequency.ui.screens.sation_lists.home

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.frequency.MainVM.Companion.GAUTH
import com.example.frequency.R
import com.example.frequency.datasource.network.CoroutineDispatcherProvider
import com.example.frequency.datasource.network.radio_browser.models.Station
import com.example.frequency.datasource.network.radio_browser.radostation_list.RadioBrowser
import com.example.frequency.foundation.model.state.ErrorModel
import com.example.frequency.foundation.model.state.UIState
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.ui.screens.sation_lists.home.home_state_data.HomeUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@SuppressLint("StaticFieldLeak")
@HiltViewModel
class HomeVM @Inject constructor(
    private val radioBrowser: RadioBrowser,
    private val shearedPreferences: AppDefaultPreferences,
    private val coroutineDispatcher: CoroutineDispatcherProvider,
) : BaseVM(), LifecycleEventObserver {

    private val _uiState = MutableStateFlow<UIState<HomeUIModel>>(UIState.Empty)
    val uiState = _uiState.asStateFlow()

    private var user = User("", "", Uri.EMPTY, "")
    private var currentOffset = 0
    private var currentTag = ""
    private var query = ""
    private var stationList = emptyList<Station?>()
    private var tagList = defaultTagsList

    init {
        updateUser()
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
        if (this.user != user) {
            this.user = user
        }
    }


    private fun getStationList() {
        if (stationList.isEmpty()) {
            loadStation()
        }
    }

    fun loadStation() {
        Log.d(TAG, "offset $currentOffset")
        _uiState.value = UIState.Pending
        viewModelScope.launch(coroutineDispatcher.IO()) {
            try {
                val list = radioBrowser.getWideSearchStation(
                    searchRequest = query,
                    offset = currentOffset,
                    tag = currentTag,
                ) ?: emptyList()
                stationList = list
                _uiState.value = UIState.Success(
                    HomeUIModel(
                        this@HomeVM.user,
                        this@HomeVM.currentOffset,
                        this@HomeVM.query,
                        this@HomeVM.stationList,
                        this@HomeVM.tagList,
                    )
                )
                delay(300)
            } catch (ex: HttpException) {
                Log.e(TAG, ex.message.toString())
                httpExceptionDialog()
            } catch (ex: IOException) {
                Log.e(TAG, ex.message.toString())
                onQueryTimeLimit()
            } catch (ex: Exception) {
                Log.e(TAG, ex.message.toString())
                onQueryTimeLimit()
            }
        }
    }

    fun riseOffset() {
        val prevValue = currentOffset
        val listSize = stationList.size
        if (listSize < 25) return
        currentOffset = listSize + prevValue
    }

    fun decreaseOffset() {
        val prevValue = currentOffset
        val listSize = stationList.size
        if (prevValue - listSize <= 0) {
            dropOffset()
        } else {
            currentOffset = prevValue - listSize
        }
    }

    fun dropOffset() {
        currentOffset = 0
    }

    fun changeTag(tag: String) {
        currentTag = tag
    }

    fun setUpdatedQuery(query: String) {
        if (this.query != query) {
            this.query = query
        }
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        when (event) {
            Lifecycle.Event.ON_CREATE -> {
                getStationList()
            }

            Lifecycle.Event.ON_DESTROY -> {
            }
            else -> {}
        }
    }

    private fun httpExceptionDialog() {
        _uiState.value = UIState.Error(
            ErrorModel(
                icon = R.drawable.ic_out_connection_24,
                title = R.string.bad_gate_way,
                message = R.string.bgw_msg,
                positive = R.string.pos_try_again,
            )
        )
    }


    private fun onQueryTimeLimit() {
        _uiState.value = UIState.Error(
            ErrorModel(
                icon = R.drawable.ic_time_24,
                title = R.string.nw_time_limit,
                message = R.string.query_limit_reached,
                positive = R.string.pos_try_again,
            )
        )
    }


    companion object {
        @JvmStatic
        private val TAG = HomeVM::class.java.simpleName

        @JvmStatic
        private val TAGS = "TAGS"

        @JvmStatic
        private val defaultTagsList = listOf(
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