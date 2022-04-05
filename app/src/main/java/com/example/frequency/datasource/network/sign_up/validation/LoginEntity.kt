package com.example.frequency.datasource.network.sign_up.validation

import android.os.Parcelable
import com.example.frequency.model.Field
import com.example.frequency.model.exception.EmptyFieldException
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginEntity(
    val email: String,
    val password: String
) : Parcelable {
    fun validate() {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)
    }
}
