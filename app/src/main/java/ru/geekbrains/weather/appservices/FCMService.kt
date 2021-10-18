package ru.geekbrains.weather.appservices

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class FCMService : FirebaseMessagingService() {

    companion object {
        const val PUSH_KEY_TITLE = "title"
        const val PUSH_KEY_MESSAGE = "message"
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
    }


    @RequiresApi(Build.VERSION_CODES.N)
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val remoteMessageData = remoteMessage.data

        if (remoteMessageData.isNotEmpty()) {
            val title = remoteMessageData[PUSH_KEY_TITLE]
            val message = remoteMessageData[PUSH_KEY_MESSAGE]
            if (!title.isNullOrBlank() && !message.isNullOrBlank()) {
                AppNotificationService(this)
                    .showNotification(
                        channelID = CHANNEL_ID_1,
                        title = title,
                        message = message,
                        priority = PRIORITY_HIGH
                    )
            }
        }
    }
}