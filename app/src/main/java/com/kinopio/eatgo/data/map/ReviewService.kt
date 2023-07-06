package com.kinopio.eatgo.data.map

import com.kinopio.eatgo.domain.map.RequestNotification
import com.kinopio.eatgo.domain.map.ReviewRequestDto
import com.kinopio.eatgo.domain.map.ReviewResponseDto
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path


interface ReviewService {


    @POST("api/v1/stores/{storeId}/reviews")
    fun createReviews(@Path("storeId") storeId: Int, @Body reviewRequestDto: ReviewRequestDto
    ) : Call<ReviewResponseDto>


    @Headers("{Authorization: key=AAAACHpPMKM:APA91bGZiS3L29ohVlv1qIljbKvG_eVQF4-UF7YcAJVaLdQRGUZbI9UU0MUlSsdNB_hxWIKEk1a9ICZhUWeTVFJ8Lrkqg15dWQ1FqqD0n5cZ_eciVy9GythXBHLa33Q3LSuGCSaZP3Jw",
        "Content-Type:application/json}")
        @POST("fcm/send")
        fun sendChatNotification( @Body requestNotification: RequestNotification?): Call<ResponseBody?>?
}