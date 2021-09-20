package ru.geekbrains.weather.view.details

import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import com.google.gson.Gson
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTO
import ru.geekbrains.weather.viewmodel.AppState
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL
import java.util.stream.Collectors
import javax.net.ssl.HttpsURLConnection

private const val YANDEX_API_KEY = "955ca124-c433-4c7d-9127-81a6c2a350e4"
private const val OPEN_WEATHER_API_KEY = "4b552c34f60eb554d5b965a03f98c6f0"

object WeatherLoadingService {

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getLines(reader: BufferedReader): String {
        return reader.lines().collect(Collectors.joining("\n"))
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun loadWeather(weather: Weather, liveData: MutableLiveData<AppState>) {
        try {
//            val uri = URL("https://api.weather.yandex.ru/v2/informers?lat=${weather.city.lat}&lon=${weather.city.lon}")
            val uri = URL("https://api.openweathermap.org/data/2.5/weather?lat=${weather.city.lat}&lon=${weather.city.lon}&units=metric&lang=ru&appid=${OPEN_WEATHER_API_KEY}")
            // api.openweathermap.org/data/2.5/weather?lat={lat}&lon={lon}&appid={API key}

            val handler = Handler(Looper.getMainLooper())
            Thread(Runnable {
                lateinit var urlConnection: HttpsURLConnection
                try {
                    urlConnection = uri.openConnection() as HttpsURLConnection
                    urlConnection.requestMethod = "GET"
//                    urlConnection.addRequestProperty(
//                        "X-Yandex-API-Key",
//                        YANDEX_API_KEY
//                    )
                    urlConnection.readTimeout = 10000
                    val bufferedReader =
                        BufferedReader(InputStreamReader(urlConnection.inputStream))
                    // преобразование ответа от сервера (JSON) в модель данных (WeatherDTO)
                    val weatherDTO: WeatherDTO = Gson().fromJson(
                        getLines(bufferedReader),
                        WeatherDTO::class.java
                    )
                    handler.post { liveData.postValue(AppState.DetailSuccess(weatherDTO)) }
                } catch (e: Exception) {
                    Log.e("", "Fail connection", e)
                    e.printStackTrace()
                    //Обработка ошибки
                    liveData.postValue(AppState.Error(e))
                } finally {
                    urlConnection.disconnect()
                }
            }).start()
        } catch (e: MalformedURLException) {
            Log.e("", "Fail URI", e)
            e.printStackTrace()
            //Обработка ошибки
        }
    }

}