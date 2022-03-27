package com.example.frequency.datasource.network.coronet

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import org.chromium.net.CronetEngine
import javax.inject.Inject

class FrequencyDataFactoryProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : ICronetEngineProvider {

    override fun getEngine(): CronetEngine {
        return CronetEngine.Builder(context).build()
    }

}