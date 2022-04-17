package com.example.frequency.di

import android.app.Application
import androidx.room.Room
import com.example.frequency.data.local.FrequencyDatabase
import com.example.frequency.data.local.dao.station.FavouriteStationsDao
import com.example.frequency.domain.repository.local.station_repository.FavouriteStationsRepository
import com.example.frequency.data.repository.local.station.FavouriteStationsRepositoryImpl
import com.example.frequency.data.local.dao.user.UserDao
import com.example.frequency.domain.repository.local.user_repository.UserRepository
import com.example.frequency.data.repository.local.user.UserRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppDatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(application: Application, ): FrequencyDatabase {
        return Room
            .databaseBuilder(application, FrequencyDatabase::class.java, "AppRoomDB")
            .build()
    }
    @Singleton
    @Provides
    fun provideUserDao(appDataBase: FrequencyDatabase): UserDao {
        return appDataBase.getUserDao()
    }

    @Singleton
    @Provides
    fun provideFavStationDao(appDataBase: FrequencyDatabase): FavouriteStationsDao {
        return appDataBase.getFavouriteStationsDao()
    }

    @Provides
    fun provideFavStationsRepository(favStationDao: FavouriteStationsDao): FavouriteStationsRepository {
        return FavouriteStationsRepositoryImpl(favStationDao)
    }

    @Provides
    fun provideUserRepository(userDao: UserDao): UserRepository {
        return UserRepositoryImpl(userDao)
    }

}