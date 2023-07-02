package com.kinopio.eatgo.domain.store

import android.net.Uri
import java.io.Serializable

data class Menu(
    val name: String,
    val count: Int,
    val price : Int,
    val info : String,
    val imageUri : String
) : Serializable