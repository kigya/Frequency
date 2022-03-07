package com.example.frequency.foundation.model

abstract class Action {
    abstract val iconRes: Int
    abstract val textRes: Int
    abstract val onCustomAction: Runnable?
}