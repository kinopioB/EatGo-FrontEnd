package com.kinopio.eatgo.domain.store

data class OpenInfoRequestDto(
    val day: String,
    val openTime: String,
    val closeTime: String
)
