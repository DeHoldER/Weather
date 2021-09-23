package ru.geekbrains.weather.view.details

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import ru.geekbrains.weather.BuildConfig
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTO
import ru.geekbrains.weather.domain.WeatherDTOConverted
import ru.geekbrains.weather.viewmodel.AppState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val OPEN_WEATHER_API_KEY = BuildConfig.OPEN_WEATHER_API_KEY //ключ для проверки 4b552c34f60eb554d5b965a03f98c6f0

object WeatherLoadingService {

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather(weather: Weather, liveData: MutableLiveData<AppState>) {
        try {
            val uri = URL("https://api.openweathermap.org/data/2.5/weather?lat=${weather.city.lat}&lon=${weather.city.lon}&units=metric&lang=ru&appid=${OPEN_WEATHER_API_KEY}")

            val handler = Handler(Looper.getMainLooper())
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    // преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
                    val weatherDTO: WeatherDTO = Gson().fromJson(
                        getLines(bufferedReader),
                        WeatherDTO::class.java
                    )
                    handler.post { liveData.postValue(AppState.DetailSuccess(WeatherDTOConverted(weatherDTO))) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    liveData.postValue(AppState.Error(e))
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            liveData.postValue(AppState.Error(e))
        }
    }

}