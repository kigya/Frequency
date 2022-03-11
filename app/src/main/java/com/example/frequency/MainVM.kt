package com.example.frequency

import androidx.lifecycle.*
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.toLD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val shearedPreferences: AppDefaultPreferences,
    private val savedStateHandle: SavedStateHandle
) : BaseVM(), LifecycleEventObserver {

    private val _userLD =
        savedStateHandle.getLiveData<User>(STATE_USER)
    val userLD = _userLD.toLD()

    private val _autologinLD = MutableLiveData(shearedPreferences.getAutologinStatus())
    val autologinLD = _autologinLD.toLD()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        updateUser()
        viewModelScope.launch {
            delay(1500)
            _isLoading.value = false
        }
    }

    private fun initializeUser(user: User) {
        if (_userLD.value != user) {
            _userLD.value = user
            savedStateHandle.set(STATE_USER, user)
        }
    }

    fun updateUser(user: User? = null) {
        if (user == null) {
            val newUserValue = User(
                shearedPreferences.getUsername(),
                shearedPreferences.getEmail(),
                shearedPreferences.getIconUri(),
                shearedPreferences.getToken(),
            )
            initializeUser(newUserValue)
        } else {
            initializeUser(user)
        }
    }

    companion object {
        //user
        @JvmStatic
        private val STATE_USER = "STATE_KEY_NAME"

        @JvmStatic
        private val STATE_KEY_NAME = "STATE_KEY_NAME"

        @JvmStatic
        private val STATE_KEY_EMAIL = "STATE_KEY_EMAIL"

        @JvmStatic
        private val STATE_KEY_ICON = "STATE_KEY_ICON"
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        //TODO("Not yet implemented")
    }

}