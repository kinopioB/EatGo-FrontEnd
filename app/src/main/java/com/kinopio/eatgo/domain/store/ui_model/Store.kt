package com.kinopio.eatgo.domain.store.ui_model

import java.io.Serializable

data class Store(
    val name: String,
    val category: String,
    val desc  : String?,
    val tags: List<Tag>?
) : Serializable