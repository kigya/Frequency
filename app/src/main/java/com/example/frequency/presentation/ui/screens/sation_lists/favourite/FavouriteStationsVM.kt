package com.example.frequency.presentation.ui.screens.sation_lists.favourite

import android.net.Uri
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.viewModelScope
import com.example.frequency.R
import com.example.frequency.data.model.User
import com.example.frequency.data.model.network.station.Station
import com.example.frequency.domain.usecase.get_station_from_local.GetFavoriteStationsUseCase
import com.example.frequency.domain.usecase.store_station_to_local.ChangeFavouriteStationStatusUseCase
import com.example.frequency.foundation.model.state.ErrorModel
import com.example.frequency.foundation.model.state.UIState
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.presentation.ui.MainVM
import com.example.frequency.presentation.ui.screens.sation_lists.favourite.model.FavouriteStationsUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class FavouriteStationsVM @Inject constructor(
    private val shearedPreferences: AppDefaultPreferences,
    private val getFavoriteStationsUseCase: GetFavoriteStationsUseCase,
    private val changeFavouriteStationStatusUseCase: ChangeFavouriteStationStatusUseCase,
) : BaseVM(), LifecycleEventObserver {

    private val _uiState = MutableStateFlow<UIState<FavouriteStationsUIModel>>(UIState.Empty)
    val uiState = _uiState.asStateFlow()

    private var user = User("", "", Uri.EMPTY, "")
    private var stationList = emptyList<Station?>()

    init {
        updateUser()
        loadFavouriteStation()
    }

    private fun updateUser(user: User? = null) {
        if (user == null) {
            val newUserValue = User(
                shearedPreferences.getUsername(),
                shearedPreferences.getEmail(),
                shearedPreferences.getIconUri(),
                if (shearedPreferences.getRegistrationType() == MainVM.GAUTH) {
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
            loadFavouriteStation()
        }
    }

    fun loadFavouriteStation() {
        Log.d(TAG, stationList.toString())
        _uiState.value = UIState.Pending
        viewModelScope.launch {
            try {
                stationList =
                    getFavoriteStationsUseCase()

                _uiState.value = UIState.Success(
                    FavouriteStationsUIModel(
                        this@FavouriteStationsVM.user,
                        this@FavouriteStationsVM.stationList,
                    )
                )
                delay(200)

            } catch (ex: IOException) {
                Log.d(TAG, ex.message.toString())
                onQueryTimeLimit()
            } catch (ex: Exception) {
                Log.d(TAG, ex.message.toString())
            }
        }
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

    companion object {
        @JvmStatic
        private val TAG = "FavouriteStationsVM"
    }

}