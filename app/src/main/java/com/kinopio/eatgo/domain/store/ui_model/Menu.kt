package com.kinopio.eatgo.domain.store.ui_model

import android.net.Uri

data class Menu(
    val name: String,
    val count: Int,
    val price : Int,
    val imageUri : Uri
)