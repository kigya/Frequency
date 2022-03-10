package com.example.frequency.screen.sign_in.model

import android.os.Parcelable
import android.provider.ContactsContract
import kotlinx.parcelize.Parcelize
import java.net.PasswordAuthentication

@Parcelize
data class LoginEntity(
    val email: String,
    val password: String
): Parcelable