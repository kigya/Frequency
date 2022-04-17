package com.example.frequency.domain.repository.remote

import com.example.frequency.data.remote.RadioBrowserApi

interface RadioBrowserService {

    fun getRadioBrowser(): RadioBrowserApi

}