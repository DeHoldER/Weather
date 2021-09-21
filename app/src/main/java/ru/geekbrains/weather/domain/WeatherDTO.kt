package ru.geekbrains.weather.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// -------------- ПОСЛЕ ПРОВЕРКИ УБРАТЬ Parcelable --------------- //
@Parcelize
data class WeatherDTO(
    val main: FactDTO?,
    val weather: Array<ConditionDTO>?
) : Parcelable

@Parcelize
data class FactDTO(
    val temp: Double?,
    val feels_like: Double?,
    val condition: String?
) : Parcelable

@Parcelize
data class ConditionDTO(
    val id: Int?,
    val description: String?,
) : Parcelable