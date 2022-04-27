package com.example.frequency.di

import com.example.frequency.data.local.FrequencyDatabase
import com.example.frequency.data.local.dao.station.DeleteStationDao
import com.example.frequency.data.local.dao.station.GetStationDao
import com.example.frequency.data.local.dao.station.StoreStationsDao
import com.example.frequency.data.repository.local.station.StationsRepositoryImpl
import com.example.frequency.domain.repository.local.station_repository.StationsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object StationLocalStorageModule {

    @Singleton
    @Provides
    fun provideFavStationDao(appDataBase: FrequencyDatabase): StoreStationsDao {
        return appDataBase.getStoreStationsDao()
    }

    @Singleton
    @Provides
    fun provideGetStationDao(appDataBase: FrequencyDatabase): GetStationDao {
        return appDataBase.getGetStationsDao()
    }

    @Singleton
    @Provides
    fun provideDeleteStationDao(appDataBase: FrequencyDatabase): DeleteStationDao {
        return appDataBase.getDeleteStationsDao()
    }

    @Singleton
    @Provides
    fun provideFavStationsRepository(
        storeStationDao: StoreStationsDao,
        getStationDao: GetStationDao,
        deleteStationDao: DeleteStationDao
    ): StationsRepository {
        return StationsRepositoryImpl(
            storeStationDao,
            getStationDao,
            deleteStationDao)
    }

}