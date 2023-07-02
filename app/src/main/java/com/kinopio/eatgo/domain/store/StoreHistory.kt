package com.kinopio.eatgo.domain.store

data class StoreHistory(
    val storeHistoryId: Integer,
    val storeId: Integer,
    val openDate: String,
    val address: String,
    val positionX: Integer,
    val positionY: Integer
)