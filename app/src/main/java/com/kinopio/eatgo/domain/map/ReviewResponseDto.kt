package com.kinopio.eatgo.domain.map

import com.google.gson.annotations.SerializedName

data class ReviewResponseDto (
    val userId:Int,
    val storeId:Int,
    val content:String,
    val rating:Int
)