package com.kinopio.eatgo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private var retrofit: Retrofit? = null
    // private val url = "http://192.168.0.145:8080/eatgo/"
    private val url = "http://10.0.2.2:8080/eatgo/"
    fun getRetrofit(): Retrofit? {
        return retrofit ?: Retrofit.Builder()
            .baseUrl(url)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val retrofit2: Retrofit = Retrofit.Builder()
        .baseUrl(url)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRetrofit2(): Retrofit = retrofit2
}