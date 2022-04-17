package com.example.frequency.di

import android.content.Context
import com.example.frequency.BuildConfig
import com.example.frequency.datasource.network.radio_browser.RadioBrowserService
import com.example.frequency.datasource.network.radio_browser.radostation_list.RadioBrowser
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
object ProvidesModule {

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
    fun provideFireBaseInterface(): FirebaseAuth {
        return Firebase.auth
    }

}