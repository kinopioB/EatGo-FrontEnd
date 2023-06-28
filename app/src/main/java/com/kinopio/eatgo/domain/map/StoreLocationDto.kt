package com.kinopio.eatgo.domain.map

import com.google.gson.annotations.SerializedName
import java.sql.Date

data class StoreLocationDto(
    @SerializedName("id") val id:Int,
    @SerializedName("name") val name:String,
    @SerializedName("userId") val userId:Int,
    @SerializedName("address") val address:String,
    @SerializedName("positionX") val positionX:Double,
    @SerializedName("positionY") val positionY:Double,
    @SerializedName("isOpen") val isOpen:Int,
    @SerializedName("categoryId") val categoryId:Int,
    @SerializedName("thumbnail") val thumbnail:String,
    @SerializedName("createdAt") val createdAt:Long
)
