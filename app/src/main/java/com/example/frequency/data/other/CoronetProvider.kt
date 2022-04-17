package com.example.frequency.data.other

import android.content.Context
import com.example.frequency.domain.ICronetEngineProvider
import dagger.hilt.android.qualifiers.ApplicationContext
import org.chromium.net.CronetEngine
import javax.inject.Inject

class CoronetProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : ICronetEngineProvider {

    override fun getEngine(): CronetEngine {
        return CronetEngine.Builder(context).build()
    }

}