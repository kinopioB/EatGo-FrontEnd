package com.kinopio.eatgo.domain.map

import com.google.gson.annotations.SerializedName

data class RequestNotification (

    @SerializedName("token") //  "to" changed to token
    val token : String,

@SerializedName("notification")
  val sendNotificationModel : SendNotificationModel

)
