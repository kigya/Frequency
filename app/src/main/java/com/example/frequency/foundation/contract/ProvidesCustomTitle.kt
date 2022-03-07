package com.example.frequency.foundation.contract

import androidx.annotation.StringRes

/**
 * Interface provides a change of a toolbar title
 * */
interface ProvidesCustomTitle {

    @StringRes
    fun getTitleRes(): Int

}