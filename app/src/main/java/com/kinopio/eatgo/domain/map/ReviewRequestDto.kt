package com.kinopio.eatgo.domain.map


data class ReviewRequestDto (
    val userId:Int,
    val storeId:Int,
    val content:String,
    val rating:Int
)