package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTO
import ru.geekbrains.weather.domain.WeatherDTOConverted

interface DetailsRepository {
    fun getWeatherDetailsFromServer(weather: Weather, callback: retrofit2.Callback<WeatherDTO>)
}