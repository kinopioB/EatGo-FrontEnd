package com.kinopio.eatgo.domain.map


data class StoreHistoryRequestDto (
    val storeId : Int,
    val address : String,
    val positionX : String,
    val positionY : String
)