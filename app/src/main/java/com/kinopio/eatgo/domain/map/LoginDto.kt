package com.kinopio.eatgo.domain.map

data class LoginDto(
    val userSocialId : String,
    val userName : String,
    val LoginType : Int,
    val role : Int,
)
