package com.example.frequency.di

import com.example.frequency.data.other.CoronetProvider
import com.example.frequency.domain.ICronetEngineProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class SingleProvidesModule {

    @Binds
    abstract fun cronetProvider(engineProvider: CoronetProvider): ICronetEngineProvider

}