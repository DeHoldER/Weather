package ru.geekbrains.weather.repository.history

import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.room.HistoryDAO
import ru.geekbrains.weather.utils.convertHistoryEntityToWeather
import ru.geekbrains.weather.utils.convertWeatherToHistoryEntity

class LocalRepositoryImpl(private val localDataSource: HistoryDAO) : LocalRepository {
    override fun getAllHistory(): List<Weather> {
        return convertHistoryEntityToWeather(localDataSource.all())
    }

    override fun saveEntity(weather: Weather) {
        localDataSource.insert(convertWeatherToHistoryEntity(weather))
    }
}