package com.example.frequency.di

import android.app.Application
import androidx.room.Room
import com.example.frequency.data.local.FrequencyDatabase
import com.example.frequency.data.local.dao.user.UserDao
import com.example.frequency.data.repository.local.user.UserRepositoryImpl
import com.example.frequency.domain.repository.local.user_repository.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FrequencyDatabaseModule {

    @Singleton
    @Provides
    fun provideFrequencyDatabase(application: Application): FrequencyDatabase {
        return Room
            .databaseBuilder(application, FrequencyDatabase::class.java, "AppRoomDB")
            .build()
    }

}