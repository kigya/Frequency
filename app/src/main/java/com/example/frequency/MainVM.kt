package com.example.frequency

import android.util.Log
import androidx.lifecycle.*
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.SnackBarEntity
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.*
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

    private val _firebaseStatus = MutableLiveData<Boolean>()
    val firebaseStatus = _firebaseStatus.share()

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
                shearedPreferences.getGToken(),
            )
            initializeUser(newUserValue)
        } else {
            initializeUser(user)
        }
    }

    private fun checkFireBaseSignIn() {
        // Check if user is signed in (non-null) and update UI accordingly.
        _firebaseStatus.value = authFirebaseAuth.currentUser != null
    }

    private fun signInWithFireBase() {
        if (autologinLD.value == true && regMethod.value != null) {
            when (regMethod.value) {
                GAUTH -> {firebaseLogin(secure = userLD.value?.secureKey.toString())}
                EMAIL_PASS -> {firebaseLogin(email = userLD.value?.email, userLD.value?.secureKey.toString())}
                FCBOOK -> {firebaseLogin(secure = userLD.value?.secureKey.toString())}
            }
        }
    }

    private fun firebaseLogin(email: String? = null, secure: String) {

        if (email != null) {
            authFirebaseAuth.signInWithEmailAndPassword(email, secure)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = authFirebaseAuth.currentUser
                        if (user != null) {
                            _firebaseStatus.value = true
                            _showSnackBar.value = Event(SnackBarEntity(R.string.auth_success, SUCCESS))
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        _showSnackBar.value = Event(SnackBarEntity(R.string.auth_fail, FAILURE))
                    }
                }
        } else {
            val credential = GoogleAuthProvider.getCredential(secure, null)
            authFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = authFirebaseAuth.currentUser
                        if (user != null) {
                            _firebaseStatus.value = true
                            _showSnackBar.value = Event(SnackBarEntity(R.string.auth_success, SUCCESS))
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        _showSnackBar.value = Event(SnackBarEntity(R.string.auth_fail, FAILURE))
                    }
                }
        }
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