package ru.geekbrains.weather.domain

data class City(
    val name: String = "Москва",
    val lon: Double = 55.755826,
    val lat: Double = 37.617299900000035,
) {
    fun getDefaultCity() = City("Москва", 55.755826, 37.617299900000035)
}