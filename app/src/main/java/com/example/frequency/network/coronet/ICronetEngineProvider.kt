package com.example.frequency.network.coronet

import org.chromium.net.CronetEngine

interface ICronetEngineProvider {

    fun getEngine(): CronetEngine

}