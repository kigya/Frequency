package com.example.frequency.services.sign_up

import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.frequency.screen.authorization.sign_up.SignUpVM.Companion.NO_ERROR_MESSAGE
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignUpState(
    @StringRes
    val emailErrorMessageRes: Int = NO_ERROR_MESSAGE,
    @StringRes
    val passwordErrorMessageRes: Int = NO_ERROR_MESSAGE,
    @StringRes
    val repeatPasswordErrorMessageRes: Int = NO_ERROR_MESSAGE,
    @StringRes
    val usernameErrorMessageRes: Int = NO_ERROR_MESSAGE,
    val signUpInProgress: Boolean = false,
) : Parcelable {
    val showProgress: Boolean get() = signUpInProgress
    val enableViews: Boolean get() = !signUpInProgress
}