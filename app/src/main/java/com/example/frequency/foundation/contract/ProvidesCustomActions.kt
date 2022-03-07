package com.example.frequency.foundation.contract

import com.example.frequency.foundation.model.Action

/**
 * Interface provides a change of a toolbar actions
 * */
interface ProvidesCustomActions {
    fun getCustomActions(): List<Action>
}