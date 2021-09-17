package ru.geekbrains.weather.viewmodel

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTO

sealed class AppState {
    data class Success(val weatherData: List<Weather>) : AppState()
    data class DetailSuccess(val weatherDTO: WeatherDTO): AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
