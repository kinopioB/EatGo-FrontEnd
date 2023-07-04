package com.kinopio.eatgo.domain.store

import java.io.Serializable

data class MenuRequestDto (
    val menuName: String,
    val info:String,
    val price : Int,
    val amount: Int,
    val thumbnail : String,
    val isBest : Int
    ) : Serializable