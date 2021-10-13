package ru.geekbrains.weather.repository.main

import ru.geekbrains.weather.domain.Weather

interface MainRepository {

    fun getWeatherFromServer() : Weather
    fun getWeatherFromLocalStorageRus() : List<Weather>
    fun getWeatherFromLocalStorageWorld() : List<Weather>

}