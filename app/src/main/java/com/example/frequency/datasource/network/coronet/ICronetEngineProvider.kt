package com.example.frequency.datasource.network.coronet

import org.chromium.net.CronetEngine

interface ICronetEngineProvider {

    fun getEngine(): CronetEngine

}