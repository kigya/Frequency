package com.example.frequency.ui.rendering

sealed class StationsUiState(){
    object Empty : StationsUiState()
    object Pending : StationsUiState()
    class Loaded(val data: StationsUiState) : StationsUiState()
    class Error(val message: String) : StationsUiState()
}