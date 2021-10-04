package ru.geekbrains.weather.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ru.geekbrains.weather.R
import ru.geekbrains.weather.view.history.HistoryFragment
import ru.geekbrains.weather.view.main.MainFragment

class MainActivity : AppCompatActivity() {
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
        menuInflater.inflate(R.menu.main_screen_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.action_open_fragment_history ->{
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, HistoryFragment.newInstance())
                    .addToBackStack("")
                    .commit()
                true
            }
            else ->super.onOptionsItemSelected(item)
        }
    }
}