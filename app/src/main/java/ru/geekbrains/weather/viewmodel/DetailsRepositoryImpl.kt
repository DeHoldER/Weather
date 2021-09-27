package ru.geekbrains.weather.viewmodel

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTO
import ru.geekbrains.weather.repository.DetailsRepository
import ru.geekbrains.weather.repository.RemoteDataSource

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource): DetailsRepository {
    override fun getWeatherDetailsFromServer(weather: Weather, callback: retrofit2.Callback<WeatherDTO>) {
        remoteDataSource.getWeatherDetails(weather,callback)
    }

}
