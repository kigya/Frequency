package com.example.frequency.domain

import org.chromium.net.CronetEngine

interface ICronetEngineProvider {

    fun getEngine(): CronetEngine

}