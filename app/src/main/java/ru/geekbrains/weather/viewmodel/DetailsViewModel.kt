package ru.geekbrains.weather.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.domain.DetailsService
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.view.details.WeatherLoadingService

class DetailsViewModel(
    private val appStateLiveData: MutableLiveData<AppState> = MutableLiveData(),
): ViewModel() {

    fun getAppState() = appStateLiveData

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateWeather(weather: Weather) {
        // -------------- ПОСЛЕ ПРОВЕРКИ ЗАМЕНИТЬ --------------- //
//        WeatherLoadingService.loadWeather(weather, appStateLiveData)
        DetailsService().loadWeather(weather.city.lat,weather.city.lon)
    }
}