package ru.geekbrains.weather.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.geekbrains.weather.MyApp.Companion.getHistoryDAO
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.domain.WeatherDTO
import ru.geekbrains.weather.domain.WeatherDTOConverted
import ru.geekbrains.weather.repository.details.DetailsRepositoryImpl
import ru.geekbrains.weather.repository.details.RemoteDataSource
import ru.geekbrains.weather.repository.history.LocalRepositoryImpl
import ru.geekbrains.weather.utils.REQUEST_ERROR
import ru.geekbrains.weather.utils.SERVER_ERROR

class DetailsViewModel(
    private val appStateLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val detailsRepositoryImpl: DetailsRepositoryImpl = DetailsRepositoryImpl(
        RemoteDataSource()
    ),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(getHistoryDAO()),
) : ViewModel() {

    fun saveWeather(weather: Weather) {
        historyRepositoryImpl.saveEntity(weather)
    }

    fun getAppState() = appStateLiveData

    @RequiresApi(Build.VERSION_CODES.N)
    fun updateWeather(weather: Weather) {
//        WeatherLoadingService.nativeRequest(weather, appStateLiveData) //старый запрос без ретрофита
        getWeatherFromRemoteSource(weather) //с ретрофитом
    }

    fun getWeatherFromRemoteSource(weather: Weather) {
        appStateLiveData.value = AppState.Loading
        detailsRepositoryImpl.getWeatherDetailsFromServer(weather, callback)
    }

    private val callback = object : retrofit2.Callback<WeatherDTO> {

        override fun onResponse(
            call: retrofit2.Call<WeatherDTO>,
            response: retrofit2.Response<WeatherDTO>
        ) {

            if (response.isSuccessful && response.body() != null) {
                val weatherDTO = response.body()
                weatherDTO?.let {
                    appStateLiveData.postValue(AppState.DetailSuccess(WeatherDTOConverted(weatherDTO)))
                }
            } else {
                appStateLiveData.postValue(AppState.Error(Throwable(SERVER_ERROR)))
            }
        }

        override fun onFailure(call: retrofit2.Call<WeatherDTO>, t: Throwable) {
            appStateLiveData.postValue(AppState.Error(Throwable(t.message ?: REQUEST_ERROR)))
        }
    }
}