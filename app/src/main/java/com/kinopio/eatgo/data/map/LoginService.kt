package com.kinopio.eatgo.data.map

import com.kinopio.eatgo.User
import com.kinopio.eatgo.domain.map.LoginDto
import com.kinopio.eatgo.domain.map.LoginResponseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("api/v1/users/login")
    fun login(@Body loginDto:LoginDto) : Call<LoginResponseDto>
}