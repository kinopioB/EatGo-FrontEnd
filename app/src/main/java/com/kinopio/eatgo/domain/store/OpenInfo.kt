package com.kinopio.eatgo.domain.store

data class OpenInfo(
    val openInfoId : Int,
    val storeId : Int,
    val day : String,
    val openTime : String,
    val closeTime : String
)
