package com.rskot.locweathermvvm.model

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import com.rskot.locweathermvvm.common.MyLocationManager
import com.rskot.locweathermvvm.model.data_class.Coord
import com.rskot.locweathermvvm.model.data_class.WeatherData
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class WeatherRepositoryInstrumentedTest {
    private lateinit var weatherRepository: WeatherRepository

    @Before
    fun createWeatherRepository() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        weatherRepository = WeatherRepositoryImpl.getInstance(appContext, MyLocationManager.getInstance(appContext))
    }

    @Test
    fun weatherInfoCached_isCorrect() {
        val weatherData = WeatherData(cityAndCountry = "Indore, IN")
        weatherRepository.setLastCachedWeatherInfo(weatherData)

        Assert.assertEquals(weatherData.toString(), weatherRepository.getLastCachedWeatherInfo().toString())
    }

    @Test
    fun lastLocation_isCorrect() {
        val coord = Coord(22.321, 75.123)
        weatherRepository.setLastLocation(coord)

        Assert.assertEquals(coord.toString(), weatherRepository.getLastLocation().toString())
    }
}