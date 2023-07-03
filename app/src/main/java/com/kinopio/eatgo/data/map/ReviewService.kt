package com.kinopio.eatgo.data.map

import com.kinopio.eatgo.domain.map.ReviewResponseDto
import com.kinopio.eatgo.domain.map.StoreMyPageResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {

    @POST("api/v1/stores/{storeId}/reviews/{userId}")
    fun createReviews(@Path("storeId") storeId: Int,
                      @Path("userId") userId: Int, @Body reviewRequestDto: ReviewResponseDto
    ) : Call<ReviewResponseDto>

    @GET("api/v1/stores/mypage/{storeId}")
    fun getReviews(@Path("storeId") storeId: Int) : Call<StoreMyPageResponseDto>


}