package com.kinopio.eatgo.domain.store


data class PopularStoreResponseDto(
    val storeId : Int,
    val storeName : String,
    val info : String,
    val thumbnail : String,
    val categoryId : Int,
    val categoryName: String,
    val reviewCount : Int
)