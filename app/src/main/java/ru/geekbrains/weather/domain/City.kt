package ru.geekbrains.weather.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class City(
    val name: String = "Москва",
    val lat: Double = 55.755826,
    val lon: Double = 37.617299900000035,
) : Parcelable