package ru.geekbrains.weather.domain

data class Weather(
    val city: City = City(),
    val temperature: Int = 0,
    val feelsLike: Int = 0,
)
