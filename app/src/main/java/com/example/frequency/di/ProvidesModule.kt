package com.example.frequency.di

import android.content.Context
import androidx.room.Room
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.preferences.Preferences
import com.example.frequency.repositorys.room.app_database.AppDB
import com.example.frequency.repositorys.room.user_db.room.UserDao
import com.example.frequency.services.radio_browser.radostation_list.RadioBrowser
import com.example.frequency.services.radio_browser.radostation_list.RadioBrowser.Companion.BASE_URL
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(ViewModelComponent::class)
class ProvidesModule {

    @Provides
    fun provideRadioBrowserService(): RadioBrowser {
        val bodyInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val headersInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val client = OkHttpClient.Builder()
            .addInterceptor(bodyInterceptor)
            .addInterceptor(headersInterceptor)
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(RadioBrowser::class.java)
    }

    @Provides
    fun providePreferences(@ApplicationContext context: Context): AppDefaultPreferences {
        return Preferences.getDefaultPreferenceInstance(context)
    }

    @Provides
    fun provideUserDao(@ApplicationContext context: Context): UserDao {
        val appRoom = Room.databaseBuilder(context, AppDB::class.java, "AppRoomDB").build()
        return appRoom.getUserDao()
    }

    @Provides
    fun provideFireBaseInterface(): FirebaseAuth {
        return Firebase.auth
    }

}