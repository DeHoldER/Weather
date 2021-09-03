package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.domain.Weather
import java.lang.Thread.sleep
import kotlin.random.Random

class MainViewModel(
    private val appStateLiveData: MutableLiveData<AppState> = MutableLiveData()
) : ViewModel() {

    fun getAppState() = appStateLiveData

    fun getWeather() = getDataFromLocalSource()

    private fun getDataFromLocalSource() {
        appStateLiveData.value = AppState.Loading
        Thread {
            sleep(1000)
            val rnd = Random.nextInt(0, 3)
            if (rnd == 0) appStateLiveData.postValue(AppState.Success(Weather()))
            else appStateLiveData.postValue(AppState.Error(Throwable("Some error")))
        }.start()
    }
}