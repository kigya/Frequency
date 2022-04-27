package com.example.frequency.di

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
object UserLocalStorageModule {

    @Singleton
    @Provides
    fun provideUserDao(appDataBase: FrequencyDatabase): UserDao {
        return appDataBase.getUserDao()
    }

    @Singleton
    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

}