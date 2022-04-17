package com.example.frequency.data.remote

import com.example.frequency.data.model.network.station.Station
import com.example.frequency.data.remote.OrderValue.FAVICON
import com.example.frequency.data.remote.OrderValue.orderList
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

object OrderValue {
    const val NAME = "name"
    const val URL = "url"
    const val HOME_PAGE = "homepage"
    const val FAVICON = "favicon"
    const val TAGS = "tags"
    const val COUNTRY = "country"
    const val STATE = "state"
    const val LANGUAGE = "language"

    @JvmStatic
    val orderList = listOf(NAME, URL, HOME_PAGE, FAVICON, TAGS, COUNTRY, STATE, LANGUAGE)
}

typealias NullableStations = List<Station?>?
typealias Stations = List<Station>

fun NullableStations.toStationsList(): Stations {
    val list = mutableListOf<Station>()
    if (!this.isNullOrEmpty()) this.forEach { if (it != null) list.add(it) }
    return list.toList()
}

interface RadioBrowserApi {

    companion object {

        const val BASE_URL = "https://radio-browser.p.rapidapi.com/"

        // path
        private const val BASE_PATH_ALL = "json"
        private const val BASE_PATH_SEARCH = "json/stations/byname/"
        private const val BASE_PATH_ROW = "json/stations/topvote/"
        private const val PATH_SEARCH = "search"
        private const val PATH_ROW_COUNT = "rowcount"
        private const val PATH_STATIONS = "stations"

        // headers & key
        private const val HEADER_HOST_NAME = "x-rapidapi-host"
        private const val HEADER_HOST_VALUE = "radio-browser.p.rapidapi.com"
        private const val HEADER_KEY_NAME = "x-rapidapi-key"
        private const val KEY_API_KEY = "6e73db0521msh311c519f94920c1p1262bfjsn03b8fd1b7fa2"
        // default value


        private const val KEY_LANGUAGE_EXACT = false
        private const val KEY_NAME_EXACT = false
        private const val KEY_COUNTRY_EXACT = false
        private const val KEY_STATE_EXACT = false
        private const val KEY_COUNTRY_CODE = "ru"
        private const val KEY_COUNTRY = ""
        private const val KEY_TAG = ""
        private const val KEY_LANGUAGE = ""
        private const val KEY_SEARCH = ""
        private const val KEY_ROWCOUNT = 25
        private const val KEY_STATE = ""
        private const val KEY_REVERSE = true
        private const val KEY_OFFSET = 0
        private const val KEY_LIMIT = 25
        private const val KEY_HIDE_BROKEN = true
    }

    //https://radio-browser.p.rapidapi.com/json/stations/search?language=russian&reverse=false&offset=0&limit=25&hidebroken=false

    @GET("$BASE_PATH_SEARCH{$PATH_SEARCH}")
    suspend fun getWideSearchStation(
        @Header(HEADER_HOST_NAME) host: String = HEADER_HOST_VALUE,
        @Header(HEADER_KEY_NAME) api_key: String = KEY_API_KEY,
        @Path(PATH_SEARCH) searchRequest: String? = KEY_SEARCH,
        @Query("name") name: String? = orderList.random(),
        @Query("nameExact") nameExact: Boolean? = KEY_NAME_EXACT,
        @Query("country") country: String? = KEY_COUNTRY,
        @Query("countryExact") countryExact: Boolean? = KEY_COUNTRY_EXACT,
        @Query("countrycode") countryCode: String? = KEY_COUNTRY_CODE,
        @Query("state") state: String? = KEY_STATE,
        @Query("stateExact") stateExact: Boolean? = KEY_STATE_EXACT,
        @Query("language") language: String? = KEY_LANGUAGE,
        @Query("languageExact") languageExact: Boolean? = KEY_LANGUAGE_EXACT,
        @Query("tag") tag: String? = KEY_TAG,
        @Query("order") order: String? = FAVICON,
        @Query("reverse") reverseOrder: Boolean? = KEY_REVERSE,
        @Query("offset") offset: Int? = KEY_OFFSET, // move by list
        @Query("limit") limit: Int? = KEY_LIMIT,
        @Query("hideBroken") hideBroken: Boolean? = true,
    ): NullableStations

    /*@Headers(
        value = ["$HEADER_HOST_NAME:$HEADER_HOST_VALUE",
            "$HEADER_KEY_NAME:$KEY_API_KEY"]
    )*/

//https://radio-browser.p.rapidapi.com/json/stations/byname/russia?order=name&reverse=true&offset=0&limit=2&hidebroken=true

    @GET("$BASE_PATH_SEARCH{$PATH_SEARCH}")
    suspend fun getSearchStation(
        @Header(HEADER_HOST_NAME) host: String = HEADER_HOST_VALUE,
        @Header(HEADER_KEY_NAME) api_key: String = KEY_API_KEY,
        @Path(PATH_SEARCH) searchRequest: String? = KEY_SEARCH,
        @Query("order") order: String? = FAVICON,
        @Query("reverse") reverseOrder: Boolean? = KEY_REVERSE,
        @Query("offset") offset: Int? = KEY_OFFSET, // move by list
        @Query("limit") limit: Int? = KEY_LIMIT,
        @Query("hideBroken") hideBroken: Boolean? = KEY_HIDE_BROKEN,
    ): NullableStations

    //https://radio-browser.p.rapidapi.com/json/stations/topvote/5?offset=0&limit=25&hidebroken=false

    @GET("$BASE_PATH_ROW{$PATH_ROW_COUNT}")
    suspend fun getTopStation(
        @Header(HEADER_HOST_NAME) host: String = HEADER_HOST_VALUE,
        @Header(HEADER_KEY_NAME) api_key: String = KEY_API_KEY,
        @Path(PATH_ROW_COUNT) rowcount: Int? = KEY_ROWCOUNT,
        @Query("offset") offset: Int? = KEY_OFFSET, // move by list
        @Query("limit") limit: Int? = KEY_LIMIT,
        @Query("hideBroken") hideBroken: Boolean? = KEY_HIDE_BROKEN,
    ): NullableStations

//https://radio-browser.p.rapidapi.com/json/stations?order=name&reverse=true&offset=0&limit=2&hidebroken=true

    @GET("$BASE_PATH_ALL/$PATH_STATIONS")
    suspend fun getStationList(
        @Header(HEADER_HOST_NAME) host: String = HEADER_HOST_VALUE,
        @Header(HEADER_KEY_NAME) api_key: String = KEY_API_KEY,
        @Query("order") order: String? = FAVICON,
        @Query("reverse") reverseOrder: Boolean? = KEY_REVERSE,
        @Query("offset") offset: Int? = KEY_OFFSET, // move by list
        @Query("limit") limit: Int? = KEY_LIMIT,
        @Query("hideBroken") hideBroken: Boolean? = KEY_HIDE_BROKEN,
    ): NullableStations

}