package com.rskot.locweathermvvm.model.data_class

/**
 * this class will be used in UI to show weather data
 */
data class WeatherData(
        var dateTime: String = "",
        var temperature: String = "0",
        var cityAndCountry: String = "",
        var weatherConditionIconUrl: String = "",
        var weatherConditionIconDescription: String = "",
        var humidity: String = "",
        var pressure: String = "",
        var visibility: String = "",
        var sunrise: String = "",
        var sunset: String = ""
) {
        override fun toString(): String {
                return "WeatherData(dateTime='$dateTime', temperature='$temperature', cityAndCountry='$cityAndCountry', weatherConditionIconUrl='$weatherConditionIconUrl', weatherConditionIconDescription='$weatherConditionIconDescription', humidity='$humidity', pressure='$pressure', visibility='$visibility', sunrise='$sunrise', sunset='$sunset')"
        }
}