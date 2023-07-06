package com.kinopio.eatgo


import com.google.gson.annotations.SerializedName
import com.kinopio.eatgo.data.store.NotificationType


data class PushNotificationEntity(

    @SerializedName("to")
    val to : String,
    @SerializedName("priority")
    val priority: String,
    @SerializedName("data")
    val data: PushNotificationData
)

data class PushNotificationData(
    @SerializedName("type")
    val type: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("message")
    val message: String
)

data class PushNotificationResponse(
    val multicastId: Long,
    val success: Int,
    val failure: Int,
    val canonicalIds: Int,
    val results: List<PushNotificationResult>
)

data class PushNotificationResult(
    val messageId: String
)