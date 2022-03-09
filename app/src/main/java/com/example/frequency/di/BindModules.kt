package com.example.frequency.di

import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.preferences.Preferences
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class BindModules {

    @Binds
    abstract fun bindPreferences(
        sharedPreferences: Preferences
    ): AppDefaultPreferences

}