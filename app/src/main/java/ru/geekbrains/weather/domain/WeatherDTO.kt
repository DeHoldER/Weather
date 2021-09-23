package ru.geekbrains.weather.domain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

// -------------- ПОСЛЕ ПРОВЕРКИ УБРАТЬ Parcelable --------------- //

// не очень понял каких комментариев нужно добавить, т.к. API по-сути конструктивно мало чем отличается от яндекса
// во всяком случае из того набора возможностей, которые я планирую использовать

// вот одно из отличий - яндекс присылает fact, в котором хранится всё, что нам надо. здесь же condition приходит в отдельном массиве !внезапно!,
// который уже хранит параметры, относящиеся к погодным условиям
@Parcelize
data class WeatherDTO(
    val main: FactDTO?,
    val weather: Array<ConditionDTO>? //хочет переопределения методов, которые, вроде как, нам не нужны
) : Parcelable

@Parcelize
data class FactDTO(
    val temp: Double?,
    val feels_like: Double?,
    val condition: String?
) : Parcelable


// собственно, condition (он же weather в респонсе)
// помимо id и description приходит параметр main, который хранит базовое описание погоды. но мы же не ищем лёгких путей,
// поэтому будем принимать расширенное описание и id, по которому будем показывать картинки погоды
// так же сюда можно получить icon. но они не очень красивые, поэтому будем рисовать свои =)
@Parcelize
data class ConditionDTO(
    val id: Int?,
    val description: String?,
) : Parcelable