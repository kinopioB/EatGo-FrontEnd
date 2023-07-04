package com.kinopio.eatgo.data.map

import com.kinopio.eatgo.domain.map.ReviewRequestDto
import com.kinopio.eatgo.domain.map.ReviewResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ReviewService {

    @POST("api/v1/stores/{storeId}/reviews/{userId}")
    fun createReviews(@Path("storeId") storeId: Int,
                      @Path("userId") userId: Int, @Body reviewRequestDto: ReviewRequestDto
    ) : Call<ReviewResponseDto>

}