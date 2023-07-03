package com.kinopio.eatgo.domain.store

data class StoreResponseDto(
    val storeId:Int,
    val storeName : String,
    val address : String,
    val positionX : Double,
    val positionY : Double,
    val isOpen : Int,
    val userId : Int,
    val userName : String,
    val categoryId : Int,
    val categoryName : String,

    val tags : List<Tag>,
    val openInfos : List<OpenInfo>,
    val ratingAverage : Float
)
