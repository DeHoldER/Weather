package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.getRussianCities
import ru.geekbrains.weather.domain.getWorldCities

class MainRepositoryImpl : MainRepository {

    override fun getWeatherFromServer(): Weather = Weather()

    override fun getWeatherFromLocalStorageRus(): List<Weather> = getRussianCities()
    override fun getWeatherFromLocalStorageWorld(): List<Weather> = getWorldCities()

}