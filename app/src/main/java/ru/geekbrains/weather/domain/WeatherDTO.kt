package ru.geekbrains.weather.domain

import ru.geekbrains.weather.R
import kotlin.math.roundToInt
import kotlin.time.microseconds

// не очень понял каких комментариев нужно добавить, т.к. API по-сути конструктивно мало чем отличается от яндекса
// во всяком случае из того набора возможностей, которые я планирую использовать

// вот одно из отличий - яндекс присылает fact, в котором хранится всё, что нам надо. здесь же condition приходит в отдельном массиве !внезапно!,
// который уже хранит параметры, относящиеся к погодным условиям
data class WeatherDTO(
    val main: FactDTO?,
    val weather: Array<ConditionDTO>? //хочет переопределения методов, которые, вроде как, нам не нужны
)

data class FactDTO(
    val temp: Double?,
    val feels_like: Double?,
    val condition: String?
)

// собственно, condition (он же weather в респонсе)
// помимо id и description приходит параметр main, который хранит базовое описание погоды. но мы же не ищем лёгких путей,
// поэтому будем принимать расширенное описание и id, по которому будем показывать картинки погоды
// так же сюда можно получить icon. но они не очень красивые, поэтому будем рисовать свои =)
data class ConditionDTO(
    val id: Int?,
    val description: String?,
    val icon: String?,
)

data class WeatherDTOConverted(
    private val weatherDTO: WeatherDTO,
    var id: Int = weatherDTO.weather?.get(0)?.id ?: 0,
    var nativeIconUrl: String = "https://openweathermap.org/img/wn/${weatherDTO.weather?.get(0)?.icon}@2x.png",
    var customIcon: Int = conditionMap[id] ?: 0,
    var condition: String = weatherDTO.weather?.get(0)?.description.toString(),
    var temp: String = formatTemp(weatherDTO.main?.temp),
    var feelsLike: String = formatTemp(weatherDTO.main?.feels_like),
)

private val conditionMap = mapOf<Int, Int>(
    0 to R.drawable.empty_pixel,
    505 to R.drawable.ic_rainy,
    800 to R.drawable.ic_clear,
    801 to R.drawable.ic_cloudy,
    804 to R.drawable.ic_overcast,
)

private fun formatTemp(number: Double?): String {
    return if (number != null) {
        val rounded = (number * 10.0).roundToInt() / 10.0
        if (rounded > 0) "+$rounded"
        else rounded.toString()
    } else "error"
}
