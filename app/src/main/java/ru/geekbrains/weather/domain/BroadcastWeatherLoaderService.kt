package ru.geekbrains.weather.domain

import android.app.IntentService
import android.content.Intent
import android.os.Handler
import android.os.Looper
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.gson.Gson
import ru.geekbrains.weather.BuildConfig
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.URL
import javax.net.ssl.HttpsURLConnection

const val LATITUDE_EXTRA = "Latitude"
const val LONGITUDE_EXTRA = "Longitude"

private const val OPEN_WEATHER_API_KEY = BuildConfig.OPEN_WEATHER_API_KEY //ключ для проверки 4b552c34f60eb554d5b965a03f98c6f0


const val DETAILS_INTENT_FILTER = "DETAILS INTENT FILTER"
const val DETAILS_LOAD_RESULT_EXTRA = "LOAD RESULT"

// -------------- ПОСЛЕ ПРОВЕРКИ УДАЛИТЬ ВЕСЬ КЛАСС ------------------- //
class DetailsService(name: String = "details") : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        intent?.let {
            val lat = it.getDoubleExtra(LATITUDE_EXTRA, -1.0)
            val lon = it.getDoubleExtra(LONGITUDE_EXTRA, -1.0)
            loadWeather(lat, lon)
        }
    }

    fun loadWeather(lat: Double, lon: Double) {
        val uri =
            URL("https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${lon}&units=metric&lang=ru&appid=$OPEN_WEATHER_API_KEY")
        Thread {
            val urlConnection = uri.openConnection() as HttpsURLConnection
            urlConnection.requestMethod = "GET"
//            urlConnection.addRequestProperty(
//                "X-Yandex-API-Key",
//                "ceae3d76-b634-4bfd-8ef5-25a327758ae9"
//            )//
            urlConnection.readTimeout = 10000
            val reader = BufferedReader(InputStreamReader(urlConnection.inputStream))
            val weatherDTO = Gson().fromJson(reader, WeatherDTO::class.java)
            val handler = Handler(Looper.getMainLooper())


            val mySendIntent = Intent(DETAILS_INTENT_FILTER)
            mySendIntent.putExtra(DETAILS_LOAD_RESULT_EXTRA, weatherDTO)
            LocalBroadcastManager.getInstance(this).sendBroadcast(mySendIntent)

            urlConnection.disconnect()
        }.start()
    }
}