package com.example.frequency.model.actions

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.frequency.foundation.model.Action

class MenuAction(
    @DrawableRes override val iconRes: Int,
    @StringRes override val textRes: Int,
    override val onCustomAction: Runnable
): Action()