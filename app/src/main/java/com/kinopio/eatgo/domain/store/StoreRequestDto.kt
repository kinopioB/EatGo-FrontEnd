package com.kinopio.eatgo.domain.store

data class StoreRequestDto(

    var storeName : String,
    var userId : Int,
    var address : String,
    var positionX : Double,
    var positionY : Double,
    var categoryId : Int,
    var thumbnail : String,
    var createdType : Int,
    var menus : List<MenuRequestDto> = mutableListOf(),
    var tags: List<TagRequestDto> = mutableListOf(),
    var openInfos : List<OpenInfoRequestDto>  = mutableListOf()

)