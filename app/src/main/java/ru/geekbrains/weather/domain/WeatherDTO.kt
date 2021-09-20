package ru.geekbrains.weather.domain

data class WeatherDTO(
    val main: FactDTO?,
    val weather: Array<ConditionDTO>?
)

data class FactDTO(
    val temp: Double?,
    val feels_like: Double?,
    val condition: String?
)

data class ConditionDTO(
    val id: Int?,
    val description: String?,
)