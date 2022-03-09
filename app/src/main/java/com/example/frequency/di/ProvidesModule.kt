package com.example.frequency.di

import android.content.Context
import androidx.room.Room
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.preferences.Preferences
import com.example.frequency.repositorys.room.AppDatabase
import com.example.frequency.repositorys.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(ViewModelComponent::class)
class ProvidesModule {

    @Provides
    fun bindPreferences(@ApplicationContext context: Context): AppDefaultPreferences {
        return Preferences.getDefaultPreferenceInstance(context)
    }

    @Provides
    fun bindUserDao(@ApplicationContext context: Context): UserDao {
        val appRoom = Room.databaseBuilder(context, AppDatabase::class.java, "AppRoomDB").build()
        return appRoom.getUserDao()
    }
    /*@Provides
      @Singleton
    fun bindWeatherDao(@ApplicationContext context: Context): UserDao {
        val appRoom = Room.databaseBuilder(context, com.example.frequency.repositorys.room.app_database.AppDatabase::class.java, "AppRoomDB").build()
        return appRoom.getUserDao()
    }*/

}