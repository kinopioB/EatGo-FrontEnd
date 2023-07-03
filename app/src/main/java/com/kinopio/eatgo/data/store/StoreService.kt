package com.kinopio.eatgo.data.store

import com.kinopio.eatgo.domain.map.LoginResponseDto
import com.kinopio.eatgo.domain.map.StoreHistoryRequestDto
import com.kinopio.eatgo.domain.templates.ApiResultDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface StoreService {
    @POST("api/v1/stores/popular")
    fun getPopularStore() : Call<LoginResponseDto>

    @POST("api/v1/stores/{storeId}/open")
    fun changeStoreStatusOpen(@Path("storeId") storeId: Int,
                              @Body storeHistoryRequestDto: StoreHistoryRequestDto ) : Call<ApiResultDto>

    // return 값 변경해줘야 함
     @POST("api/v1/stores/{storeId}/close")
    fun changeStoreStatusClose(@Path("storeId") storeId: Int) : Call<ApiResultDto>
}