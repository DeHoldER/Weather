package ru.geekbrains.weather.viewmodel

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTOConverted

sealed class AppState {
    data class SuccessMain(val weatherData: List<Weather>) : AppState()
    data class SuccessHistory(val weatherData: List<Weather>) : AppState()
    data class DetailSuccess(val weatherData: WeatherDTOConverted) : AppState()
    data class Error(val error: Throwable) : AppState()
    object Loading : AppState()
}
