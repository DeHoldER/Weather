package ru.geekbrains.weather.appservices

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import ru.geekbrains.weather.R

const val CHANNEL_ID_1 = "CHANNEL_ID_1"
const val CHANNEL_ID_2 = "CHANNEL_ID_2"

const val PRIORITY_DEFAULT = 0
const val PRIORITY_MIN = 1
const val PRIORITY_LOW = 2
const val PRIORITY_HIGH = 3
const val PRIORITY_MAX = 4

//public static final int IMPORTANCE_NONE = 0;
//public static final int IMPORTANCE_MIN = 1;
//public static final int IMPORTANCE_LOW = 2;
//public static final int IMPORTANCE_DEFAULT = 3;
//public static final int IMPORTANCE_HIGH = 4;
//public static final int IMPORTANCE_MAX = 5;

//public static final int PRIORITY_DEFAULT = 0;
//public static final int PRIORITY_LOW = -1;
//public static final int PRIORITY_MIN = -2;
//public static final int PRIORITY_HIGH = 1;
//public static final int PRIORITY_MAX = 2;


class AppNotificationService(private val context: Context) {

    private val notificationIDs = mutableListOf<Int>()

    @RequiresApi(Build.VERSION_CODES.N)
    private fun convertPriority(priority: Int, isSDKHigh: Boolean): Int {
        if (isSDKHigh) {
            return when (priority) {
                PRIORITY_DEFAULT -> NotificationManager.IMPORTANCE_DEFAULT
                PRIORITY_MIN -> NotificationManager.IMPORTANCE_MIN
                PRIORITY_LOW -> NotificationManager.IMPORTANCE_LOW
                PRIORITY_HIGH -> NotificationManager.IMPORTANCE_HIGH
                PRIORITY_MAX -> NotificationManager.IMPORTANCE_MAX
                else -> NotificationManager.IMPORTANCE_DEFAULT
            }
        } else {
            return when (priority) {
                PRIORITY_DEFAULT -> NotificationCompat.PRIORITY_DEFAULT
                PRIORITY_MIN -> NotificationCompat.PRIORITY_MIN
                PRIORITY_LOW -> NotificationCompat.PRIORITY_LOW
                PRIORITY_HIGH -> NotificationCompat.PRIORITY_HIGH
                PRIORITY_MAX -> NotificationCompat.PRIORITY_MAX
                else -> NotificationCompat.PRIORITY_DEFAULT
            }
        }
    }

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @RequiresApi(Build.VERSION_CODES.N)
    fun showNotification(
        channelID: String,
        title: String,
        message: String,
        priority: Int,
        messageID: Int = notificationIDs.size
    ) {

        val notificationBuilder = NotificationCompat.Builder(context, channelID)
            .apply {
                setSmallIcon(R.drawable.ic_map_marker)
                setContentTitle(title)
                setContentText(message)
                this.priority = convertPriority(priority, false)
            }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannel = "Name $channelID"
            val descriptionChannel = "Description $channelID"
            val importanceChannel = convertPriority(priority, true)
            val notification = NotificationChannel(
                channelID,
                nameChannel,
                importanceChannel
            ).apply { description = descriptionChannel }
            notificationManager.createNotificationChannel(notification)
        }
        notificationManager.notify(messageID, notificationBuilder.build())
        notificationIDs.add(notificationIDs.size)

    }

}