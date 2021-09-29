package ru.geekbrains.weather.repository.details

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.geekbrains.weather.BuildConfig
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTO
import ru.geekbrains.weather.utils.OPEN_WEATHER_API_BASE_URL
import ru.geekbrains.weather.utils.OPEN_WEATHER_API_LANGUAGE_RU
import ru.geekbrains.weather.utils.OPEN_WEATHER_API_UNITS_METRIC

class RemoteDataSource {

    private val weatherApi by lazy {
        Retrofit
            .Builder()
            .baseUrl(OPEN_WEATHER_API_BASE_URL)
            .addConverterFactory(
                GsonConverterFactory.create(
                    GsonBuilder()
                        .setLenient()
                        .create()
                )
            )
            .build()
            .create(WeatherAPI::class.java)
    }


    fun getWeatherDetails(weather: Weather, callback: retrofit2.Callback<WeatherDTO>) {
        weatherApi.getWeather(
            weather.city.lat,
            weather.city.lon,
            OPEN_WEATHER_API_UNITS_METRIC,
            OPEN_WEATHER_API_LANGUAGE_RU,
            BuildConfig.OPEN_WEATHER_API_KEY
        )
            .enqueue(callback)
    }
}