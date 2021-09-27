package ru.geekbrains.weather.repository

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.geekbrains.weather.domain.WeatherDTO
import ru.geekbrains.weather.utils.OPEN_WEATHER_API_URL_END_POINT

interface WeatherAPI {
    @GET(OPEN_WEATHER_API_URL_END_POINT)
    fun getWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("units") units: String,
        @Query("lang") language: String,
        @Query("appid") apiKey: String,
    ): Call<WeatherDTO>
}
