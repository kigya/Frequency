package com.example.frequency.data.model.sign_up.validation

import com.example.frequency.data.model.utilitar.Field
import com.example.frequency.data.model.utilitar.exception.EmptyFieldException
import com.example.frequency.data.model.utilitar.exception.PasswordMismatchException

/**
 * Fields that should be provided during creating a new account.
 */
data class SignUpData(
    val username: String,
    val email: String,
    val password: String,
    val repeatPassword: String
) {
    fun validate() {
        if (email.isBlank()) throw EmptyFieldException(Field.Email)
        if (username.isBlank()) throw EmptyFieldException(Field.Username)
        if (password.isBlank()) throw EmptyFieldException(Field.Password)
        if (password != repeatPassword) throw PasswordMismatchException()
    }
}