package com.rskot.locweathermvvm.model.data_class

import org.junit.Assert
import org.junit.Test

class WeatherInfoResponseUnitTest {

    @Test
    fun weatherInfoResponseMapping_isCorrect() {
        val sys = Sys(country = "IN")
        val response = WeatherInfoResponse(name = "Indore", sys = sys)
        val weatherData = response.transformToWeatherData();
        Assert.assertEquals("Indore, IN", weatherData.cityAndCountry)
    }
}