package com.example.frequency.screen.home

import com.example.frequency.foundation.views.BaseVM
import com.example.frequency.utils.MutableLiveEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeVM @Inject constructor(): BaseVM() {

    val fooLD = MutableLiveEvent<Unit>()

}