package com.example.frequency.foundation.contract

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

/**
 * Interface provides a change of a toolbar actions
 * */
interface HasCustomAction {

    fun getCustomAction(): CustomAction

}

class CustomAction(
    @DrawableRes val iconRes: Int,
    @StringRes val textRes: Int,
    val onCustomAction: Runnable
)