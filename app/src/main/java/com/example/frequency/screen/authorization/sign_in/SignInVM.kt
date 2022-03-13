package com.example.frequency.screen.authorization.sign_in

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.frequency.MainVM.Companion.EMAIL_PASS
import com.example.frequency.R
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.Field
import com.example.frequency.model.SnackBarEntity
import com.example.frequency.model.User
import com.example.frequency.model.exception.AuthException
import com.example.frequency.model.exception.EmptyFieldException
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.services.sign_up.validation.SignInState
import com.example.frequency.utils.*
import com.example.frequency.utils.SummaryUtils.FAILURE
import com.example.frequency.utils.SummaryUtils.SUCCESS
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInVM @Inject constructor(
    private val authFirebaseAuth: FirebaseAuth,
    private val sharedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val _state = MutableLiveData(SignInState())
    val state = _state.share()

    private val _navigateToHome = MutableLiveEvent<User>()
    val navigateToHome = _navigateToHome.share()

    private val _showSnackBar = MutableLiveEvent<SnackBarEntity>()
    val showSnackBar = _showSnackBar.share()

    private val _currentUserLD = savedStateHandle.getLiveData<User>(STATE_KEY_USER)
    val currentUserLD = _currentUserLD.share()

    private val _clearPasswordEvent = MutableUnitLiveEvent()
    val clearPasswordEvent = _clearPasswordEvent.share()

    private fun updateUser(
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
            dataValidation(email, password)
            loginWithFirebase(email, password)
            delay(200)
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: AuthException) {
            processAuthException()
        }
    }

    private fun loginWithFirebase(email: String, password: String) {
        authFirebaseAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = authFirebaseAuth.currentUser
                    if (user != null) {
                        addToShearedPrefs(
                            username = user.displayName,
                            email = email,
                            password = password
                        )
                        updateUser(
                            user.displayName ?: email.substringBefore("@"),
                            email,
                            user.photoUrl ?: Uri.EMPTY,
                            password
                        )
                        _showSnackBar.value = Event(SnackBarEntity(R.string.auth_success, SUCCESS))
                        _navigateToHome.provideEvent(currentUserLD.value!!)
                        hideProgress()
                    }
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    _showSnackBar.value = Event(SnackBarEntity(R.string.auth_fail, FAILURE))
                    hideProgress()
                }
            }
    }

    private fun addToShearedPrefs(username: String? = null, email: String, password: String) {
        sharedPreferences.setUsername(username ?: email.substringBefore("@"))
        sharedPreferences.setEmail(email)
        sharedPreferences.setPassword(password)
        sharedPreferences.setRegistrationType(EMAIL_PASS)
    }

    private fun dataValidation(email: String, password: String) {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isEmpty()) throw EmptyFieldException(Field.Password)
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.value = _state.requireValue().copy(
            emptyEmailError = e.field == Field.Email,
            emptyPasswordError = e.field == Field.Password,
            signInInProgress = false
        )
    }

    private fun processAuthException() {
        _state.value = _state.requireValue().copy(
            signInInProgress = false
        )
        _clearPasswordEvent.provideEvent()
    }


    private fun showProgress() {
        _state.value = SignInState(signInInProgress = true)
    }

    private fun hideProgress() {
        _state.value = _state.requireValue().copy(signInInProgress = false)
    }

    companion object {
        @JvmStatic
        private val STATE_KEY_USER = "STATE_KEY_USER"

        @JvmStatic
        private val TAG = SignInFragment::class.java.simpleName

        @JvmStatic
        private val STATE_KEY_LOGIN_TUPLES = "STATE_KEY_LOGIN_TUPLES"
    }

}