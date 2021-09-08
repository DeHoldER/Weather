package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather

interface Repository {

    fun getWeatherFromServer() : Weather
    fun getWeatherFromLocalStorageRus() : List<Weather>
    fun getWeatherFromLocalStorageWorld() : List<Weather>

}