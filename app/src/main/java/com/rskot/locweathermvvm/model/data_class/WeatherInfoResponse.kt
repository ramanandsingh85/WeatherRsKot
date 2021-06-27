package com.rskot.locweathermvvm.model.data_class

import com.google.gson.annotations.SerializedName
import com.rskot.locweathermvvm.utils.kelvinToCelsius
import com.rskot.locweathermvvm.utils.unixTimestampToDateTimeString
import com.rskot.locweathermvvm.utils.unixTimestampToTimeString

data class WeatherInfoResponse(
    @SerializedName("coord")
        val coord: Coord = Coord(),
    @SerializedName("weather")
        val weather: List<Weather> = listOf(),
    @SerializedName("base")
        val base: String = "",
    @SerializedName("main")
        val main: Main = Main(),
    @SerializedName("visibility")
        val visibility: Int = 0,
    @SerializedName("wind")
        val wind: Wind = Wind(),
    @SerializedName("clouds")
        val clouds: Clouds = Clouds(),
    @SerializedName("dt")
        val dt: Int = 0,
    @SerializedName("sys")
        val sys: Sys = Sys(),
    @SerializedName("id")
        val id: Int = 0,
    @SerializedName("name")
        val name: String = "",
    @SerializedName("cod")
        val cod: Int = 0
) {
    fun transformToWeatherData(): WeatherData {
        // business logic and data manipulation tasks should be done here
        return WeatherData(
            dateTime = dt.unixTimestampToDateTimeString(),
            temperature = main.temp.kelvinToCelsius().toString(),
            cityAndCountry = "${name}, ${sys.country}",
            weatherConditionIconUrl = if (!weather.isEmpty()) "http://openweathermap.org/img/w/${weather[0].icon}.png" else "",
            weatherConditionIconDescription = if (!weather.isEmpty()) weather[0].description else "",
            humidity = "${main.humidity}%",
            pressure = "${main.pressure} mBar",
            visibility = "${visibility/1000.0} KM",
            sunrise = sys.sunrise.unixTimestampToTimeString(),
            sunset = sys.sunset.unixTimestampToTimeString()
        )
    }
}

