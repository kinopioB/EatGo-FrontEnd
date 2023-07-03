package com.kinopio.eatgo.data.map

import com.kinopio.eatgo.domain.map.StoreLocationDto
import com.kinopio.eatgo.domain.map.StoreLocationListDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface StoreLocationService {
    @GET("api/v1/stores")
    fun getStores() :Call<List<StoreLocationDto>>

    @GET("api/v1/stores/search/{searchFilter}")
    fun getFilterStores(@Path("searchFilter") searchFilter : String) : Call<List<StoreLocationDto>>

}