package com.example.frequency.data.model.sign_up

import android.os.Parcelable
import androidx.annotation.StringRes
import com.example.frequency.presentation.ui.screens.authorization.sign_up.SignUpVM.Companion.NO_ERROR_MESSAGE
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