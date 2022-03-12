package com.example.frequency.screen.authorization.sign_up

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
import com.example.frequency.model.exception.AccountAlreadyExistsException
import com.example.frequency.model.exception.EmptyFieldException
import com.example.frequency.model.exception.PasswordMismatchException
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.services.sign_up.SignUpState
import com.example.frequency.services.sign_up.validation.SignUpData
import com.example.frequency.utils.*
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpVM @Inject constructor(
    private val authFirebaseAuth: FirebaseAuth,
    private val sharedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle,
) : BaseVM() {

    private val _state = MutableLiveData(SignUpState())
    val state = _state.share()

    private val _navigateToHome = MutableLiveEvent<User>()
    val navigateToHome = _navigateToHome.share()

    private val _showSnackBar = MutableLiveEvent<SnackBarEntity>()
    val showSnackBar = _showSnackBar.share()

    private val _currentUserLD = savedStateHandle.getLiveData<User>(STATE_KEY_USER)
    val currentUserLD = _currentUserLD.share()

    private val _regMethod = savedStateHandle.getLiveData<Int>(STATE_REG_METHOD)
    val regMethod = _regMethod.share()

    fun createAccount(signUpData: SignUpData) = viewModelScope.launch {
        showProgress()
        try {
            signUpData.validate()
            firebaseCreationAcc(signUpData.username, signUpData.email, signUpData.password)
            delay(300)
        } catch (e: EmptyFieldException) {
            processEmptyFieldException(e)
        } catch (e: PasswordMismatchException) {
            processPasswordMismatchException()
        } catch (e: AccountAlreadyExistsException) {
            processAccountAlreadyExistsException()
        }
    }

    private fun firebaseCreationAcc(username: String, email: String, password: String) {
        authFirebaseAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = authFirebaseAuth.currentUser
                    _currentUserLD.value = User(
                        name = username,
                        email = user?.email.toString(),
                        icon = user?.photoUrl ?: Uri.EMPTY,
                        secureKey = password
                    )
                    writeShearedPreferences()
                    _showSnackBar.value = Event(SnackBarEntity(R.string.sign_up_success, SUCCESS))
                    _navigateToHome.provideEvent(currentUserLD.value!!)
                    hideProgress()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    _showSnackBar.value =
                        Event(SnackBarEntity(R.string.create_account_failure, FAILURE))
                }
            }
    }

    private fun writeShearedPreferences() {
        sharedPreferences.setUsername(currentUserLD.value?.name.toString())
        sharedPreferences.setEmail(currentUserLD.value?.email.toString())
        sharedPreferences.setIconUri(currentUserLD.value?.icon ?: Uri.EMPTY)
        sharedPreferences.setPassword(currentUserLD.value?.secureKey.toString())
        sharedPreferences.setRegistrationType(EMAIL_PASS)
    }

    private fun processEmptyFieldException(e: EmptyFieldException) {
        _state.value = when (e.field) {
            Field.Email -> _state.requireValue()
                .copy(emailErrorMessageRes = R.string.field_is_empty)
            Field.Username -> _state.requireValue()
                .copy(usernameErrorMessageRes = R.string.field_is_empty)
            Field.Password -> _state.requireValue()
                .copy(passwordErrorMessageRes = R.string.field_is_empty)
        }
    }

    private fun processPasswordMismatchException() {
        _state.value = _state.requireValue()
            .copy(repeatPasswordErrorMessageRes = R.string.password_mismatch)
    }

    private fun processAccountAlreadyExistsException() {
        _state.value = _state.requireValue()
            .copy(emailErrorMessageRes = R.string.account_already_exists)
    }

    private fun showProgress() {
        _state.value = _state.requireValue().copy(signUpInProgress = true)
        Log.d(TAG, "SHOW")
    }

    private fun hideProgress() {
        _state.value = _state.requireValue().copy(signUpInProgress = false)
        Log.d(TAG, "HIDE")
    }

    companion object {
        @JvmStatic
        val NO_ERROR_MESSAGE = 0

        @JvmStatic
        private val TAG = SignUpVM::class.java.simpleName

        @JvmStatic
        private val STATE_KEY_USER = "STATE_KEY_USER"

        @JvmStatic
        private val STATE_REG_METHOD = "STATE_REG_METHOD"

    }


}