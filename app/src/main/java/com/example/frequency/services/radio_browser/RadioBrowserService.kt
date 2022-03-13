package com.example.frequency.services.radio_browser

import com.example.frequency.services.radio_browser.radostation_list.RadioBrowser
import com.example.frequency.services.radio_browser.radostation_list.RadioBrowser.Companion.BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RadioBrowserService @Inject constructor() : RadioBrowserWrapper {

    override fun getRadioBrowser(): RadioBrowser {

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

}