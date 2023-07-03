package com.kinopio.eatgo.domain.store

import com.kinopio.eatgo.domain.store.ui_model.OpenInfo
import com.kinopio.eatgo.domain.store.ui_model.Tag

data class StoreDetailResponseDto(

    val storeId: Int,
    val storeName: String,
    val address: String,
    val positionX: Double,
    val positionY : Double,
    val isOpen : Integer,
    val thumbnail : String,
    val createdType :Int,
    val createdAt : String,

    val userId : Int,
    val userName : String,

    val categoryId : Int,
    val categoryName: String,

    val tags : List<Tag>,
    val openInfos : List<OpenInfo>,
    val storeHistories : List<StoreHistory>,
    val menus : List<Menu>,
    val reviews : List<ReviewDto>,

    // 리뷰 평점 평균
    val ratingAverage : Float

)
