package com.example.frequency.screen.welcome

import android.net.Uri
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.lifecycle.SavedStateHandle
import com.example.frequency.R
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.SnackBarEntity
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.utils.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WelcomeVM @Inject constructor(
    private val authFireBase: FirebaseAuth,
    private val sharedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val _showPbLd = MutableLiveEvent<Boolean>()
    val showPbLd = _showPbLd.toLD()

    private val _navigateToHome = MutableLiveEvent<User>()
    val navigateToHome = _navigateToHome.toLD()

    private val _showSnackBar = MutableLiveEvent<SnackBarEntity>()
    val showSnackBar = _showSnackBar.toLD()

    private val _currentUserLD = savedStateHandle.getLiveData<User>(STATE_KEY_USER)
    val currentUserLD = _currentUserLD.toLD()

    private fun showPb(state: Boolean){
        _showPbLd.value = Event(state)
    }

    private fun registerUser(
        name: String,
        email: String,
        icon: Uri,
        gToken: String
    ) {
        _currentUserLD.value = User(name, email, icon, gToken)
    }

    private fun addUserToShearedPrefs(
        name: String,
        email: String,
        icon: Uri,
        gToken: String
    ) {
        sharedPreferences.setUsername(name)
        sharedPreferences.setEmail(email)
        sharedPreferences.setIconUri(icon)
        sharedPreferences.setToken(gToken)
    }

    private fun provideDataFirebase(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        authFireBase.signInWithCredential(credential).addOnCompleteListener {
            val user = currentUserLD.value
            if (it.isSuccessful) {
                if (user != null) {
                    addUserToShearedPrefs(user.name, user.email, user.icon, user.gToken)
                    _navigateToHome.value = Event(user)
                }
                showPb(false)
                _showSnackBar.value = Event(SnackBarEntity(R.string.auth_success, SUCCESS))
            } else {
                showPb(false)
                _showSnackBar.value = Event(SnackBarEntity(R.string.auth_fail, FAILURE))
            }
        }
    }

    fun getAccount(result: ActivityResult) {
        showPb(true)
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                registerUser(
                    account.displayName.toString(),
                    account.email.toString(),
                    account.photoUrl ?: Uri.EMPTY,
                    account.idToken.toString(),
                )
                provideDataFirebase(account.idToken.toString())
            } else {
                Log.d("WelcomeVM", "account == null")
                showPb(false)
                _showSnackBar.value = Event(SnackBarEntity(R.string.auth_fail, ERROR))
            }
        } catch (e: ApiException) {
            showPb(false)
            _showSnackBar.value = Event(SnackBarEntity(R.string.auth_fail, ERROR))
        }
    }

    companion object {
        @JvmStatic
        private val STATE_KEY_USER = "STATE_KEY_USER"
    }

}