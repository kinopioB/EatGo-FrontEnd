package com.kinopio.eatgo.domain.store.ui_model

data class Review(
    val userId : Int,
    val storeId : Int,
    val userName : String,
    val content: String,
    val rating : Int,
    val createdAt: String
)