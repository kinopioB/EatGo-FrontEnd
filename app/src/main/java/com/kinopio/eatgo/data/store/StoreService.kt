package com.kinopio.eatgo.data.store

import com.kinopio.eatgo.domain.map.LoginDto
import com.kinopio.eatgo.domain.map.LoginResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface StoreService {
    @POST("api/v1/stores/popular")
    fun getPopularStore() : Call<LoginResponseDto>
}