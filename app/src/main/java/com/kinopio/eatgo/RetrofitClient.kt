package com.kinopio.eatgo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null
    fun getRetrofit(): Retrofit? {
        return retrofit ?: Retrofit.Builder()
            .baseUrl("http://192.168.0.120:8080/eatgo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofit2: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.0.120:8080/eatgo/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRetrofit2(): Retrofit = retrofit2
}