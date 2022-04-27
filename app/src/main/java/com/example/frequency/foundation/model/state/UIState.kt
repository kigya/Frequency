package com.example.frequency.foundation.model.state

sealed class UIState<out T>(open val data: T? = null) {
    object Empty : UIState<Nothing>()
    object Pending : UIState<Nothing>()
    class Success<T>(override val data: T) : UIState<T>(data)
    class Error(val error: ErrorModel) : UIState<Nothing>()
}