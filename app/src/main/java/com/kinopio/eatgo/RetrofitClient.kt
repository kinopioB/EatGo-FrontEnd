package com.kinopio.eatgo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit: Retrofit? = null

    fun getRetrofit(): Retrofit? {
        return retrofit ?: Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8080/eatgo/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}