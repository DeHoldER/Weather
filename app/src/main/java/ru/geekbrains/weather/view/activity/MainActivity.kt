package ru.geekbrains.weather.view.activity

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import ru.geekbrains.weather.R
import ru.geekbrains.weather.contentProvider.ContentProviderFragment
import ru.geekbrains.weather.lesson10.MapsFragment
import ru.geekbrains.weather.showFragment
import ru.geekbrains.weather.utils.FRAGMENT_CONTAINER
import ru.geekbrains.weather.view.history.HistoryFragment
import ru.geekbrains.weather.view.main.MainFragment
import ru.geekbrains.weather.view.map.WeatherMapFragment

class MainActivity : AppCompatActivity() {

    companion object {
        private const val CHANNEL_ID_1 = "CHANNEL_ID_1"
        private const val CHANNEL_ID_2 = "CHANNEL_ID_2"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkStateReceiver, filter)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance())
//                .addToBackStack(null)
                .commit()
        }

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notificationBuilder_1 = NotificationCompat.Builder(this, CHANNEL_ID_1).apply {
            setSmallIcon(R.drawable.ic_map_marker)
            setContentTitle("Заголовок для $CHANNEL_ID_1")
            setContentText("Сообщение $CHANNEL_ID_1")
            priority = NotificationCompat.PRIORITY_MAX
        }
        val notificationBuilder_2 = NotificationCompat.Builder(this, CHANNEL_ID_2).apply {
            setSmallIcon(R.drawable.ic_map_pin)
            setContentTitle("Заголовок для $CHANNEL_ID_2")
            setContentText("Сообщение $CHANNEL_ID_2")
            priority = NotificationCompat.PRIORITY_LOW
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannel_1 = "Name $CHANNEL_ID_1"
            val descriptionChannel_1 = "Description $CHANNEL_ID_1"
            val importanceChannel_1 = NotificationManager.IMPORTANCE_MIN
            val channel_1 = NotificationChannel(
                CHANNEL_ID_1,
                nameChannel_1,
                importanceChannel_1
            ).apply { description = descriptionChannel_1 }
            notificationManager.createNotificationChannel(channel_1)
        }
        notificationManager.notify(1, notificationBuilder_1.build())
        notificationManager.notify(2, notificationBuilder_1.build())
        notificationManager.notify(3, notificationBuilder_1.build())


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val nameChannel_2 = "Name $CHANNEL_ID_2"
            val descriptionChannel_2 = "Description $CHANNEL_ID_2"
            val importanceChannel_2 = NotificationManager.IMPORTANCE_HIGH
            val channel_2 = NotificationChannel(
                CHANNEL_ID_2,
                nameChannel_2,
                importanceChannel_2
            ).apply { description = descriptionChannel_2 }
            notificationManager.createNotificationChannel(channel_2)
        }
        notificationManager.notify(4, notificationBuilder_2.build())
        notificationManager.notify(5, notificationBuilder_2.build())
        notificationManager.notify(6, notificationBuilder_2.build())
    }



    private var networkStateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val noConnectivity =
                intent.getBooleanExtra(ConnectivityManager.EXTRA_NO_CONNECTIVITY, false)
            if (!noConnectivity) {
                onConnectionFound()
            } else {
                onConnectionLost()
            }
        }
    }

    fun onConnectionLost() {
        Toast.makeText(this, "Connection lost", Toast.LENGTH_LONG).show()
    }

    fun onConnectionFound() {
        Toast.makeText(this, "Connection found", Toast.LENGTH_LONG).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkStateReceiver)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_screen_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_open_fragment_history -> {
                supportFragmentManager.showFragment(
                    FRAGMENT_CONTAINER, HistoryFragment.newInstance(), "history"
                )
                true
            }
            R.id.action_open_fragment_google_maps -> {
                supportFragmentManager.showFragment(
                    FRAGMENT_CONTAINER, WeatherMapFragment.newInstance(), "maps"
                )
                true
            }
            R.id.action_open_fragment_content_provider -> {
                supportFragmentManager.showFragment(
                    FRAGMENT_CONTAINER, ContentProviderFragment.newInstance(), "contacts"
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}
