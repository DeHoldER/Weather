package ru.geekbrains.weather.view.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.messaging.FirebaseMessaging
import ru.geekbrains.weather.R
import ru.geekbrains.weather.contentProvider.ContentProviderFragment
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

    @RequiresApi(Build.VERSION_CODES.N)
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

//        val appNotificationService = AppNotificationService(this).apply {
//            showNotification(
//                CHANNEL_ID_1,
//                "Заголовок 1",
//                "Вот такое вот замечательное сообщение 1",
//                NotificationCompat.PRIORITY_MAX,
//                1
//            )
//        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { it ->
            if (it.isSuccessful) Log.d("myLogs", it.result.toString())
        }
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
