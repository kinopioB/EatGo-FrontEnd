package com.kinopio.eatgo.data.store

import com.kinopio.eatgo.domain.map.LoginResponseDto
import com.kinopio.eatgo.domain.map.StoreHistoryRequestDto
import com.kinopio.eatgo.domain.templates.ApiResultDto
import com.kinopio.eatgo.domain.store.PopularStoreResponseDto
import com.kinopio.eatgo.domain.store.StoreDetailResponseDto
import com.kinopio.eatgo.domain.store.StoreRequestDto
import com.kinopio.eatgo.domain.store.TodayOpenStoreResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StoreService {

    @POST("api/v1/stores/{storeId}/open")
    fun changeStoreStatusOpen(@Path("storeId") storeId: Int,
                              @Body storeHistoryRequestDto: StoreHistoryRequestDto ) : Call<ApiResultDto>

    // return 값 변경해줘야 함
     @POST("api/v1/stores/{storeId}/close")
    fun changeStoreStatusClose(@Path("storeId") storeId: Int) : Call<ApiResultDto>

    @GET("api/v1/stores/popular")
    fun getPopularStore() : Call<List<PopularStoreResponseDto>>

    @GET("api/v1/stores/today-open")
    fun getTodayOpenStores() : Call<List<TodayOpenStoreResponseDto>>

    @POST("api/v1/stores")
    fun createStore(@Body storeRequestDto: StoreRequestDto) : Call<StoreDetailResponseDto>
}