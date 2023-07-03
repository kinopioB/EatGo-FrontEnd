package com.kinopio.eatgo.domain.map

import com.kinopio.eatgo.domain.store.ui_model.Review
import com.kinopio.eatgo.domain.store.ui_model.Tag

data class StoreMyPageResponseDto (
    val storeId:Int,
    val userName: String,
    val storeName: String,
    val thumbNail: String,

    val categoryId : Int,
    val categoryName : String,
    val isOpen : Int,

    val ratingAverage: Float,

    val tags : List<Tag>,
    val reviews : List<Review>
    )