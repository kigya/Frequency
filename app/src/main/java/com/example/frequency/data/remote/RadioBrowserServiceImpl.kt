package com.example.frequency.data.remote

import com.example.frequency.data.remote.RadioBrowserApi.Companion.BASE_URL
import com.example.frequency.domain.repository.remote.RadioBrowserService
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class RadioBrowserServiceImpl @Inject constructor() : RadioBrowserService {

    override fun getRadioBrowser(): RadioBrowserApi {

        val bodyInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val headersInterceptor =
            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.HEADERS)

        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                chain.proceed(
                    chain.request().newBuilder().header("User-Agent", "Frequency v0.1").build()
                )
            }
            .addInterceptor(bodyInterceptor)
            .addInterceptor(headersInterceptor)
            .build()


        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        return retrofit.create(RadioBrowserApi::class.java)
    }

    companion object {
        const val NETWORK_REQUEST_TIMEOUT_SECONDS = 15L
    }
}