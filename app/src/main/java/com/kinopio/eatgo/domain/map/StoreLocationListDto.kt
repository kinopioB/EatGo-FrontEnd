package com.kinopio.eatgo.domain.map

import com.google.gson.annotations.SerializedName

data class StoreLocationListDto(
    @SerializedName("res") val res:List<StoreLocationDto>
)
