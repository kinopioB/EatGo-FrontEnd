package com.kinopio.eatgo.data.store

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.kinopio.eatgo.MainActivity
import com.kinopio.eatgo.R

class MyFirebaseMessagingService : FirebaseMessagingService() {
    private val TAG = "FirebaseService"

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d(TAG, "From: " + message.from)
        super.onMessageReceived(message)

        createNotificationChannel() // 채널생성함수 호출

        // 알림이 어떤 타입인지 구분 - 타입에 따라 메세지 형태가 다름

        var type = message.data["type"]?.let { NotificationType.valueOf(it) }
        type = NotificationType.NORMAL
        val title = message.data["title"]
        val message = message.data["body"]
        Log.d(TAG, "onMessageReceived : ${title}")
        Log.d(TAG, "message : ${message}")
        type ?: return
        Log.d(TAG, "특정 권한 예외 처리 전!")

        // 특정 권한을 요청하고 있는데 사용자가 권한을 거부할 수 있는 상황에서
        // 명시적으로 확인 및 예외 처리하는 과정
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.d(TAG, "특정 권한 예외 처리 후!")

            return
        }

        if (type != null) {
            NotificationManagerCompat.from(this)
                .notify(type.id, createNotification(type, title, message))
        }
    }

    // 채널 생성하는 함수
    private fun createNotificationChannel() {
        Log.d(TAG, "채널 생성 함수 진입 !")

        // 오레오 버전 이후에는 채널이 필요
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            channel.description = CHANNEL_DESCRIPTION
            Log.d(TAG, "채널 생성 함수 진입 버전 조건 확인 !")

            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // 알림을 생성하는 함수
    // 파라미터 1. 알림 타입, 2. 제목, 3. 메세지
    private fun createNotification(type: NotificationType?, title:String?, message: String?) : Notification {

        // 알림을 누르면 실행될 수 있게 intent를 만들어 줌
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra(TAG,"${type?.title} 타입")
            addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) // 이미 해당 액티비티일때 인텐트가 연결되면 1개만 유지함
        }

        // 일회용 PendingIntent
        // PendingIntent : Intent의 실행 권한을 외부의 어플리케이션에게 위임
        val resultPendingIntent = PendingIntent.getActivity(this,type!!.id,intent, FLAG_UPDATE_CURRENT)

        // 기본 빌더 - 알림에 대한 UI 정보와 작업을 지정
        val builder =  NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.star_icon) // 아이콘 설정
            .setContentTitle(title) // 제목
            .setContentText(message) // 메세지
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(resultPendingIntent) // 알림을 누를경우 생기는 인탠트
            .setAutoCancel(true)

        Log.d("firebase",type?.title.toString())

        // 빌더에 type별로 속성을 추가해준다.
        when(type){
            NotificationType.NORMAL -> Unit

            // 많은 text가 올 경우
            NotificationType.EXPANDABLE -> {
               /* builder.setStyle(NotificationCompat.BigTextStyle()
                    .bigText(bigEmoji))*/
            }

            //layout을 통해 custom하기
            NotificationType.CUSTOM -> {
                /*builder.setStyle(NotificationCompat.DecoratedCustomViewStyle())
                    .setCustomContentView(
                        RemoteViews(packageName,R.layout.view_custom_notification)
                        .apply {
                            setTextViewText(R.id.title, textTitle)
                            setTextViewText(R.id.message, body)
                        })*/
            }
        }
        // build()하여 반환함으로써 notify에서 한번에 사용
        return builder.build()
    }
    companion object {
        private const val CHANNEL_NAME = "EatGo"
        private const val CHANNEL_DESCRIPTION = "EatGo Alarm Channel"
        private const val CHANNEL_ID = "채널 ID"
    }
}