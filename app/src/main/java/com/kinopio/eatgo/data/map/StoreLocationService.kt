package com.kinopio.eatgo.data.map

import com.kinopio.eatgo.domain.map.StoreLocationDto
import com.kinopio.eatgo.domain.map.StoreLocationListDto
import retrofit2.Call
import retrofit2.http.GET

interface StoreLocationService {
    @GET("api/v1/stores")
    fun getStores() :Call<List<StoreLocationDto>>
}