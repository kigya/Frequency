package com.example.frequency.network.sign_up.validation

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SignInState(
    val emptyEmailError: Boolean = false,
    val emptyPasswordError: Boolean = false,
    val signInInProgress: Boolean = false
): Parcelable {
    val showProgress: Boolean get() = signInInProgress
    val enableViews: Boolean get() = !signInInProgress
}