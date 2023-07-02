package com.kinopio.eatgo.domain.store.ui_model

import java.io.Serializable

data class Tag(
    val tagId: Int,
    val storeId : Int,
    val tagName: String
) : Serializable