package com.kinopio.eatgo.domain.store

import com.kinopio.eatgo.domain.store.ui_model.OpenInfo

data class StoreModificationResponseDto(
    var storeId : Int,
    var storeInfo : String,
    var address : String,
    var positionX : Double,
    var positionY : Double,
    var createdType : Int,

    var openInfos : List<OpenInfo>
)
