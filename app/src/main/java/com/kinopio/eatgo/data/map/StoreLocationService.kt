package com.kinopio.eatgo.data.map

import com.kinopio.eatgo.domain.map.StoreLocationDto
import com.kinopio.eatgo.domain.map.StoreLocationListDto
import com.kinopio.eatgo.domain.store.StoreResponseDto
import com.kinopio.eatgo.presentation.map.DistanceResponseDto
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StoreLocationService {
    @GET("api/v1/stores")
    fun getStores() :Call<List<StoreLocationDto>>

    @GET("api/v1/stores/search/{searchFilter}")
    fun getFilterStores(@Path("searchFilter") searchFilter : String) : Call<List<StoreLocationDto>>

    @GET("api/v1/stores/{storeId}")
    fun getSummaryStore(@Path("storeId") storeId : Int) : Call<StoreResponseDto>

    @GET("https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&mode=transit&region=KR&language=ko")
    fun getDistance(
        @Query("origins") origins: String,
        @Query("destinations") destinations: String,
        @Query("key") key : String
    ) : Call<DistanceResponseDto>
}