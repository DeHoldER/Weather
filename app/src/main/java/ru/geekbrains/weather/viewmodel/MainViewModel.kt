package ru.geekbrains.weather.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainViewModel(
    private val appStateLiveData: LiveData<AppState> = MutableLiveData()
) : ViewModel() {

    fun getAppStateLiveData() = appStateLiveData

}