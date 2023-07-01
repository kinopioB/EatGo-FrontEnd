package com.kinopio.eatgo.domain.map

data class LoginResponseDto(
    val userId : Int,
    val userSocialId : String,
    val userName : String,
    val jwt : String,
    val loginType: Int,
    val role : Int
)
