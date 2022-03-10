package com.example.frequency.screen.sign_in.model

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

    override fun emailValidation(email: String) {

    }

    override fun checkPassword(password: String) {

    }

    override fun login(email: String, password: String) {

    }

    override fun provideTuples(loginEntity: LoginEntity) {

    }

}