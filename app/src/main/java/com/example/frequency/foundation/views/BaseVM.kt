package com.example.frequency.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.frequency.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * This LD wrappers are provided to reduce code when wrapping actions that need to happen once
 */

abstract class BaseVM : ViewModel()