package com.example.frequency.data.other

import kotlinx.coroutines.Dispatchers

class DispatcherProvider {

    fun IO() = Dispatchers.IO

    fun Default() = Dispatchers.Default

    fun Main() = Dispatchers.Main
}