package com.example.frequency.services.sign_up.model

import com.example.frequency.services.sign_up.validation.LoginEntity

class LoginService : ILoginService {

    companion object {

        //private var rootPreferences: SharedPreferences? = null
        private var instance: ILoginService? = null

        fun getLoginServiceInstance(): ILoginService {
            if (instance == null) {
                instance = LoginService()
                //rootPreferences = PreferenceManager.getDefaultSharedPreferences(context)
            }
            return instance!!
        }
    }

    fun emailValidation(email: String) {
    }


    fun checkPassword(password: String) {
    }


    fun login(email: String, password: String) {
    }


    fun provideTuples(loginEntity: LoginEntity) {
    }
}