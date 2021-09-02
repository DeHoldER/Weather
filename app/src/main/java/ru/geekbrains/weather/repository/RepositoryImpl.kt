package ru.geekbrains.weather.repository

import ru.geekbrains.weather.domain.Weather

class RepositoryImpl : Repository {
    override fun getWeatherFromLocal(): Weather {
        return Weather()
    }

    override fun getWeatherFromServer(): Weather {
        return Weather()
    }
}