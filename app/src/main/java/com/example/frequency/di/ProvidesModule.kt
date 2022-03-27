package com.example.frequency.di

import android.content.Context
import androidx.room.Room
import com.example.frequency.BuildConfig
import com.example.frequency.datasource.network.radio_browser.radostation_list.RadioBrowser
import com.example.frequency.datasource.network.radio_browser.radostation_list.RadioBrowser.Companion.BASE_URL
import com.example.frequency.preferences.AppDefaultPreferences
import com.example.frequency.preferences.Preferences
import com.example.frequency.datasource.local.repositories.AppDB
import com.example.frequency.datasource.local.repositories.room.user_db.room.UserDao
import com.example.frequency.datasource.network.CoroutineDispatcherProvider
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ProvidesModule {

    @Provides
    fun provideRadioBrowserService(): RadioBrowser {
        val bodyInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val headersInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val client = OkHttpClient.Builder()
            .connectTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_REQUEST_TIMEOUT_SECONDS, TimeUnit.SECONDS)
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

    @Provides
    fun providePreferences(@ApplicationContext context: Context): AppDefaultPreferences {
        return Preferences.getDefaultPreferenceInstance(context)
    }

    @Provides
    fun provideUserDao(@ApplicationContext context: Context): UserDao {
        val appRoom = Room
            .databaseBuilder(context, AppDB::class.java, "AppRoomDB")
            .build()
        return appRoom.getUserDao()
    }

    @Provides
    fun provideFireBaseInterface(): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun provideCoroutineDispatcher() = CoroutineDispatcherProvider()

    companion object{
        val NETWORK_REQUEST_TIMEOUT_SECONDS = 15L
    }

}