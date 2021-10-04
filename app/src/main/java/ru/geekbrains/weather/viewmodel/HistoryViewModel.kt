package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.geekbrains.weather.MyApp
import ru.geekbrains.weather.domain.Weather
import ru.geekbrains.weather.repository.history.LocalRepositoryImpl

class HistoryViewModel(
    private val historyLiveData: MutableLiveData<AppState> = MutableLiveData(),
    private val historyRepositoryImpl: LocalRepositoryImpl = LocalRepositoryImpl(MyApp.getHistoryDAO())
): ViewModel() {

    fun getAllHistory() {
        historyLiveData.value = AppState.Loading
        historyLiveData.value = AppState.SuccessHistory(historyRepositoryImpl.getAllHistory())
    }

    fun getLiveData() = historyLiveData
}