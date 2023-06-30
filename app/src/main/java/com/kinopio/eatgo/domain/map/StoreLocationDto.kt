package com.kinopio.eatgo.domain.map

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class StoreLocationDto(
    @SerializedName("storeId") val id:Int,
    @SerializedName("storeName") val name:String,
    @SerializedName("address") val address:String,
    @SerializedName("positionX") val positionX:Double,
    @SerializedName("positionY") val positionY:Double,
    @SerializedName("isOpen") val isOpen:Int,
    @SerializedName("categoryId") val categoryId:Int,
    @SerializedName("createdType") val createdType:Int
)
