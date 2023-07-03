package com.kinopio.eatgo.domain.store

data class ReviewDto(
    val userId: Integer,
    val storeId: Integer,
    val userName: String,
    val content: String,
    val rating: Integer,
    val createdAt: String
)
