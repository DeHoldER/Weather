package ru.geekbrains.weather.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.view.details.WeatherLoadingService

class DetailsViewModel(
    private val appStateLiveData: MutableLiveData<AppState> = MutableLiveData(),
) : ViewModel() {

    fun getAppState() = appStateLiveData

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateWeather(weather: Weather) {
        WeatherLoadingService.loadWeather(weather, appStateLiveData)
    }
}