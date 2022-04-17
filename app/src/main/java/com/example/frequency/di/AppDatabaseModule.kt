package com.example.frequency.di

import android.app.Application
import androidx.room.Room
import com.example.frequency.datasource.local.AppDatabase
import com.example.frequency.datasource.local.repositories.favourite_stations_db.FavouriteStationsDao
import com.example.frequency.datasource.local.repositories.favourite_stations_db.repository.FavouriteStationsRepository
import com.example.frequency.datasource.local.repositories.favourite_stations_db.repository.FavouriteStationsRepositoryImpl
import com.example.frequency.datasource.local.repositories.user_db.UserDao
import com.example.frequency.datasource.local.repositories.user_db.repository.UserRepository
import com.example.frequency.datasource.local.repositories.user_db.repository.UserRepositoryImpl
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
    fun provideAppDatabase(application: Application, ): AppDatabase {
        return Room
            .databaseBuilder(application, AppDatabase::class.java, "AppRoomDB")
            .build()
    }
    @Singleton
    @Provides
    fun provideUserDao(appDataBase: AppDatabase): UserDao {
        return appDataBase.getUserDao()
    }

    @Singleton
    @Provides
    fun provideFavStationDao(appDataBase: AppDatabase): FavouriteStationsDao {
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