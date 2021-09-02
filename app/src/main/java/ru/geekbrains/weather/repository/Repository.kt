package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather

interface Repository {

    fun getWeatherFromLocal() : Weather

    fun getWeatherFromServer() : Weather
}