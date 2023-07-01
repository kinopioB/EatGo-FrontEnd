package com.kinopio.eatgo.domain.store

data class PopularStoreResponseDto(
    val userId : Int,
    val userSocialId : String,
    val userName : String,
    val jwt : String,
    val loginType: Int,
    val role : Int
)