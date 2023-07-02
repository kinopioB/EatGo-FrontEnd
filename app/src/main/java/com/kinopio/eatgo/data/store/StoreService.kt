package com.kinopio.eatgo.data.store

import com.kinopio.eatgo.domain.store.PopularStoreResponseDto
import com.kinopio.eatgo.domain.store.TodayOpenStoreResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface StoreService {
    @GET("api/v1/stores/popular")
    fun getPopularStore() : Call<List<PopularStoreResponseDto>>

    @GET("api/v1/stores/today-open")
    fun getTodayOpenStores() : Call<List<TodayOpenStoreResponseDto>>
}