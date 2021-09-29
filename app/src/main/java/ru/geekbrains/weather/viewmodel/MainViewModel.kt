package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.repository.main.MainRepository
import ru.geekbrains.weather.repository.main.MainRepositoryImpl

class MainViewModel(
    private val appStateLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val mainRepositoryImpl: MainRepository = MainRepositoryImpl()
) : ViewModel() {

    fun getAppState() = appStateLiveData

    fun getWeatherFromLocalSourceRus() = getDataFromLocalSource(isRussian = true)
    fun getWeatherFromLocalSourceWorld() = getDataFromLocalSource(isRussian = false)

    fun getWeatherFromRemoteSource() = getDataFromLocalSource(isRussian = true)

    private fun getDataFromLocalSource(isRussian: Boolean) {
        appStateLiveData.postValue(
            AppState.Success(
                if (isRussian)
                    mainRepositoryImpl.getWeatherFromLocalStorageRus()
                else
                    mainRepositoryImpl.getWeatherFromLocalStorageWorld()
            )
        )
    }
}