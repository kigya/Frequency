package com.example.frequency.foundation.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

class CustomAction(
    @DrawableRes override val iconRes: Int,
    @StringRes override val textRes: Int,
    override val onCustomAction: Runnable
): Action()