package com.example.frequency.screen.authorization.sign_in

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.SnackBarEntity
import com.example.frequency.model.User
import com.example.frequency.model.exception.AuthException
import com.example.frequency.model.exception.EmptyFieldException
import com.example.frequency.model.exception.StorageException
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.services.sign_up.validation.LoginState
import com.example.frequency.utils.MutableLiveEvent
import com.example.frequency.utils.MutableUnitLiveEvent
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInVM @Inject constructor(
    private val sharedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val _showPbLd = MutableLiveEvent<Boolean>()
    val showPbLd = _showPbLd.share()

    private val _navigateToHome = MutableLiveEvent<User>()
    val navigateToHome = _navigateToHome.share()

    private val _showSnackBar = MutableLiveEvent<SnackBarEntity>()
    val showSnackBar = _showSnackBar.share()

    private val _currentUserLD = savedStateHandle.getLiveData<User>(STATE_KEY_USER)
    val currentUserLD = _currentUserLD.share()

    private val _state = MutableLiveData(LoginState())
    val state = _state.share()

    private val _clearPasswordEvent = MutableUnitLiveEvent()
    val clearPasswordEvent = _clearPasswordEvent.share()

    private val _showAuthErrorSnackEvent = MutableLiveEvent<Int>()
    val showAuthErrorSnackEvent = _showAuthErrorSnackEvent.share()

    fun updateUser(
        name: String,
        email: String,
        icon: Uri,
        gToken: String
    ) {
        _currentUserLD.value = User(name, email, icon, gToken)
    }

    fun login(email: String, password: String) = viewModelScope.launch {
        showProgress()
        try {
            //accountsRepository.signIn(email, password)
        } catch (e: EmptyFieldException) {
            //processEmptyFieldException(e)
        } catch (e: AuthException) {
            //processAuthException()
        } catch (e: StorageException) {
            //processStorageException()
        }
    }

    private fun showProgress() {
        _state.value = LoginState(signInInProgress = true)
    }

    companion object {
        @JvmStatic
        private val STATE_KEY_USER = "STATE_KEY_USER"

        @JvmStatic
        private val STATE_KEY_LOGIN_TUPLES = "STATE_KEY_LOGIN_TUPLES"
    }

}