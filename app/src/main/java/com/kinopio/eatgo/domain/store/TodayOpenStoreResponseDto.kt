package com.kinopio.eatgo.domain.store

import com.kinopio.eatgo.domain.store.ui_model.Tag


data class TodayOpenStoreResponseDto(
    val storeId : Int,
    val storeName : String,
    val info : String,
    val categoryId : Int,
    val categoryName: String,
    val tags : List<Tag>
)