package com.kinopio.eatgo.domain.store.ui_model

import android.net.Uri
import java.io.Serializable

data class MenuForm(
    val name: String,
    val amount: Int,
    val price : Int,
    val info : String,
    val imageUri : Uri?,
    val isBest : Int
) : Serializable