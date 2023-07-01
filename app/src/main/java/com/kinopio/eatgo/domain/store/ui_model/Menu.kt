package com.kinopio.eatgo.domain.store.ui_model

import android.net.Uri
import java.io.Serializable

data class Menu(
    val name: String,
    val count: Int,
    val price : Int,
    val info : String,
    val imageUri : String
) : Serializable