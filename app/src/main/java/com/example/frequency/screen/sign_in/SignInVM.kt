package com.example.frequency.screen.sign_in

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.model.User
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.screen.sign_in.model.LoginEntity
import com.example.frequency.utils.share
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SignInVM @Inject constructor(
    private val sharedPreferences: AppDefaultPreferences,
    savedStateHandle: SavedStateHandle
) : BaseVM() {

    private val _registerUserLD = savedStateHandle.getLiveData<User>(STATE_KEY_USER)
    val registerUserLD = _registerUserLD.share()

    private val _loginTuples = savedStateHandle.getLiveData<LoginEntity>(STATE_KEY_LOGIN_TUPLES)
    private val loginTuples = _loginTuples.share()

    // private val loginService = TODO

    fun updateUser(
        name: String,
        email: String,
        icon: Uri,
        gToken: String
    ) {
        _registerUserLD.value = User(name, email, icon, gToken)
    }

    fun login(loginEntity: LoginEntity){

    }

    companion object {
        @JvmStatic
        private val STATE_KEY_USER = "STATE_KEY_USER"

        @JvmStatic
        private val STATE_KEY_LOGIN_TUPLES = "STATE_KEY_LOGIN_TUPLES"
    }

}