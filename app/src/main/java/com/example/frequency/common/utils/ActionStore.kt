package com.example.frequency.common.utils

import com.example.frequency.R
import com.example.frequency.data.model.utilitar.actions.MenuAction
import com.example.frequency.data.model.utilitar.actions.ProfileAction

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