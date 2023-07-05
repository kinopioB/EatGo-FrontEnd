package com.kinopio.eatgo.domain.map


data class StoreHistoryRequestDto (
    val storeId : Int,
    val address : String,
    val positionX : Double,
    val positionY : Double
)