package com.example.frequency.screen.sign_in.model

interface ILoginService {

    fun emailValidation(email: String)

    fun checkPassword(password: String)

    fun login(email: String, password: String)

    fun provideTuples(loginEntity: LoginEntity)

}