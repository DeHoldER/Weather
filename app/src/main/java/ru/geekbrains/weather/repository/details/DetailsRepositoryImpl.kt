package ru.geekbrains.weather.repository.details

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTO
import ru.geekbrains.weather.repository.details.DetailsRepository
import ru.geekbrains.weather.repository.details.RemoteDataSource

class DetailsRepositoryImpl(private val remoteDataSource: RemoteDataSource): DetailsRepository {
    override fun getWeatherDetailsFromServer(weather: Weather, callback: retrofit2.Callback<WeatherDTO>) {
        remoteDataSource.getWeatherDetails(weather,callback)
    }

}
