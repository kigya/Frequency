package com.example.frequency.di

import com.example.frequency.network.coronet.FrequencyDataFactoryProvider
import com.example.frequency.network.coronet.ICronetEngineProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class SingleProvidesModule {

    @Binds
    abstract fun cronetProvider(engineProvider: FrequencyDataFactoryProvider): ICronetEngineProvider

}