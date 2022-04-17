package com.example.frequency.di

import com.example.frequency.data.other.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object CoroutineDispatchersModule {

    @Provides
    fun provideCoroutineDispatcher() = DispatcherProvider()

}