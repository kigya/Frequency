package com.example.frequency.utils

import com.example.frequency.R
import com.example.frequency.model.actions.MenuAction
import com.example.frequency.model.actions.ProfileAction

object ActionStore {

    @JvmStatic
    val menuAction = MenuAction(
        iconRes = R.drawable.ic_navigation_menu,
        textRes = R.string.menu,
        null
    )

    fun provideProfileAction(runnable: Runnable) = ProfileAction(
            iconRes = R.drawable.ic_unknown_user_photo,
            textRes = R.string.profile,
            runnable
        )

}