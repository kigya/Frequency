package com.example.frequency

import android.util.Log
import androidx.lifecycle.*
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.SnackBarEntity
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.services.sign_up.validation.SignInState
import com.example.frequency.utils.*
import com.example.frequency.utils.SummaryUtils.SUCCESS
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainVM @Inject constructor(
    private val authFirebaseAuth: FirebaseAuth,
    private val shearedPreferences: AppDefaultPreferences,
    private val savedStateHandle: SavedStateHandle
) : BaseVM(), LifecycleEventObserver {

    private val _state = MutableLiveData(SignInState())
    val state = _state.share()

    private val _navigateToWelcome = MutableUnitLiveEvent()
    val navigateToWelcome = _navigateToWelcome.share()

    private val _navigateToHome = MutableUnitLiveEvent()
    val navigateToHome = _navigateToHome.share()

    private val _regMethod = savedStateHandle.getLiveData<Int>(STATE_REG_METHOD)
    val regMethod = _regMethod.share()

    private val _userLD = savedStateHandle.getLiveData<User>(STATE_USER)
    val userLD = _userLD.share()

    private val _autologinLD = MutableLiveData(shearedPreferences.getAutologinStatus())
    val autologinLD = _autologinLD.share()

    private val _showSnackBar = MutableLiveEvent<SnackBarEntity>()
    val showSnackBar = _showSnackBar.share()

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    init {
        _regMethod.value = shearedPreferences.getRegistrationType()
        updateUser()
        viewModelScope.launch {
            delay(1500)
            _isLoading.value = false
        }
    }

    fun updateUser(user: User? = null) {
        if (user == null) {
            val newUserValue = User(
                shearedPreferences.getUsername(),
                shearedPreferences.getEmail(),
                shearedPreferences.getIconUri(),
                if (regMethod.value == GAUTH) {
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


    fun signInWithFireBase() {
        val email = userLD.value?.email
        val secureKey = userLD.value?.secureKey.toString().trim()
        Log.w(TAG, "$email, $secureKey")
        showProgress()

        if (secureKey.isNotBlank() && autologinLD.value == true && regMethod.value != null) {
            when (regMethod.value) {
                GAUTH -> {
                    firebaseLogin(secure = secureKey)
                }
                EMAIL_PASS -> {
                    firebaseLogin(email, secureKey)
                }
                FCBOOK -> {
                    firebaseLogin(secure = secureKey)
                }
            }
        } else {
            _showSnackBar.value = Event(SnackBarEntity(R.string.reg_needed))
            _navigateToWelcome.provideEvent()
            hideProgress()
        }
    }

    private fun firebaseLogin(email: String? = null, secure: String) {
        viewModelScope.launch {
            if (!email.isNullOrBlank()) {
                authFirebaseAuth.signInWithEmailAndPassword(email, secure)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = authFirebaseAuth.currentUser
                            if (user != null) {
                                _showSnackBar.value =
                                    Event(SnackBarEntity(R.string.auth_success, SUCCESS))
                                _navigateToHome.provideEvent()
                                hideProgress()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.exception)
                            _showSnackBar.value = Event(SnackBarEntity(R.string.reg_needed))
                            _navigateToWelcome.provideEvent()
                            hideProgress()
                        }
                    }
            } else {
                val credential = GoogleAuthProvider.getCredential(secure, null)
                authFirebaseAuth.signInWithCredential(credential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithEmail:success")
                            val user = authFirebaseAuth.currentUser
                            if (user != null) {
                                _showSnackBar.value =
                                    Event(SnackBarEntity(R.string.auth_success, SUCCESS))
                                _navigateToHome.provideEvent()
                                hideProgress()
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithGoogle:failure", task.exception)
                            _showSnackBar.value = Event(SnackBarEntity(R.string.reg_needed))
                            _navigateToWelcome.provideEvent()
                            hideProgress()
                        }
                    }
            }
            delay(300)
        }
    }

    private fun showProgress() {
        _state.value = SignInState(signInInProgress = true)
    }

    private fun hideProgress() {
        _state.value = _state.requireValue().copy(signInInProgress = false)
    }


    companion object {
        //user
        @JvmStatic
        private val STATE_USER = "STATE_KEY_NAME"

        @JvmStatic
        private val STATE_REG_METHOD = "STATE_REG_METHOD"

        @JvmStatic
        private val STATE_KEY_EMAIL = "STATE_KEY_EMAIL"

        @JvmStatic
        private val STATE_KEY_ICON = "STATE_KEY_ICON"

        @JvmStatic
        private val TAG = this::class.java.simpleName

        @JvmStatic
        val EMAIL_PASS = 1000

        @JvmStatic
        val GAUTH = 1001

        @JvmStatic
        val FCBOOK = 1002

    }

    override fun onCleared() {
        super.onCleared()
        authFirebaseAuth.signOut()
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        TODO("Not yet implemented")
    }

}