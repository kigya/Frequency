package com.example.frequency.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.frequency.BuildConfig
import com.example.frequency.datasource.local.repositories.AppDataBase
import com.example.frequency.datasource.local.repositories.favourite_stations_db.FavouriteStationsDao
import com.example.frequency.datasource.local.repositories.user_db.room.UserDao
import com.example.frequency.datasource.network.CoroutineDispatcherProvider
import com.example.frequency.datasource.network.radio_browser.RadioBrowserService
import com.example.frequency.datasource.network.radio_browser.radostation_list.RadioBrowser
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.preferences.Preferences
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class ProvidesModule {

    @Singleton
    @Provides
    fun provideRadioBrowserService(): RadioBrowser {
        return RadioBrowserService().getRadioBrowser()
    }

    @Provides
    fun provideGoogleClient(@ApplicationContext context: Context): GoogleSignInClient {
        val gOptions = GoogleSignInOptions
            .Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.firebase_api_key)
            .requestProfile()
            .requestEmail()
            .build()

        return GoogleSignIn.getClient(
            context,
            gOptions
        )
    }

    @Singleton
    @Provides
    fun providePreferences(@ApplicationContext context: Context): AppDefaultPreferences {
        return Preferences.getDefaultPreferenceInstance(context)
    }

    @Provides
    @Singleton
    fun provideAppDatabase(
        application: Application,
    ): AppDataBase{
        return Room
            .databaseBuilder(application, AppDataBase::class.java, "AppRoomDB")
            .build()
    }
    @Singleton
    @Provides
    fun provideUserDao(appDataBase: AppDataBase): UserDao {
        return appDataBase.getUserDao()
    }

    @Singleton
    @Provides
    fun provideFavStationDao(appDataBase: AppDataBase): FavouriteStationsDao {
        return appDataBase.getFavouriteStationsDao()
    }

    @Singleton
    @Provides
    fun provideFireBaseInterface(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun provideCoroutineDispatcher() = CoroutineDispatcherProvider()


}