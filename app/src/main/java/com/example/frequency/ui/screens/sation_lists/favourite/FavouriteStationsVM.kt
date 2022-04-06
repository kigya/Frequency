package com.example.frequency.ui.screens.sation_lists.favourite

import com.example.frequency.foundation.views.BaseVM
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class FavouriteStationsVM: BaseVM() {

    private val _uiState = MutableStateFlow(){}
    val uiState = _uiState.asStateFlow()



}