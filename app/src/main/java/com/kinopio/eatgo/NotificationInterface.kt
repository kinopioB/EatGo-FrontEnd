package com.kinopio.eatgo


import retrofit2.Call
import retrofit2.http.Body

import retrofit2.http.POST




interface NotificationInterface {
    @POST("fcm/send")
    fun sendPushNotification(@Body pushNotificationEntity: PushNotificationEntity): Call<PushNotificationResponse>
}