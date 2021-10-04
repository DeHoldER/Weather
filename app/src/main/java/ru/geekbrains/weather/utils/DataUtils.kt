package ru.geekbrains.weather.utils

import ru.geekbrains.weather.domain.City
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.room.HistoryEntity

fun convertHistoryEntityToWeather(entityList: List<HistoryEntity>): List<Weather> {
    return entityList.map {
        Weather(
            city = City(it.name),
            temperature = it.temperature,
            condition = it.condition
        )
    }
}

fun convertWeatherToHistoryEntity(weather: Weather): HistoryEntity {
    return HistoryEntity(0,weather.city.name,weather.temperature,weather.condition)
}