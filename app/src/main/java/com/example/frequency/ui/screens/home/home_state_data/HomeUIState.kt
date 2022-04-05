package com.example.frequency.ui.screens.home.home_state_data

sealed class HomeUIState {
    object Empty : HomeUIState()
    object Pending : HomeUIState()
    class Loaded(val data: HomeUIModel) : HomeUIState()
    class Error(val data: HomeErrorModel) : HomeUIState()
}
