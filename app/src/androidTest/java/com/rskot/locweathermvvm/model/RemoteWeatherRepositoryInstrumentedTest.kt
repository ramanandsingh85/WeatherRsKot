package com.rskot.locweathermvvm.model

import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.rskot.locweathermvvm.common.MyLocationManager
import com.rskot.locweathermvvm.model.data_class.Coord
import com.rskot.locweathermvvm.model.data_class.WeatherInfoResponse
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class RemoteWeatherRepositoryInstrumentedTest {
    private lateinit var weatherRepository: WeatherRepository
    private lateinit var remoteWeatherRepository: RemoteWeatherRepository
    private val gson = GsonBuilder().setLenient().create()

    private val mockWebServer = MockWebServer()

    @Before
    fun createWeatherRepository() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        weatherRepository = WeatherRepositoryImpl.getInstance(appContext, MyLocationManager.getInstance(appContext))
        remoteWeatherRepository = RemoteWeatherRepositoryImpl(appContext, weatherRepository)

        mockWebServer.start(0)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun getWeatherInfoSyncSuccess_isCorrect() {
        val mockResponse = MockResponse()
        mockResponse.setResponseCode(200)
        val body = "{\"coord\": { \"lon\": 139,\"lat\": 35},\n" +
                "  \"weather\": [\n" +
                "    {\n" +
                "      \"id\": 800,\n" +
                "      \"main\": \"Clear\",\n" +
                "      \"description\": \"clear sky\",\n" +
                "      \"icon\": \"01n\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"base\": \"stations\",\n" +
                "  \"main\": {\n" +
                "    \"temp\": 281.52,\n" +
                "    \"feels_like\": 278.99,\n" +
                "    \"temp_min\": 280.15,\n" +
                "    \"temp_max\": 283.71,\n" +
                "    \"pressure\": 1016,\n" +
                "    \"humidity\": 93\n" +
                "  },\n" +
                "  \"wind\": {\n" +
                "    \"speed\": 0.47,\n" +
                "    \"deg\": 107.538\n" +
                "  },\n" +
                "  \"clouds\": {\n" +
                "    \"all\": 2\n" +
                "  },\n" +
                "  \"dt\": 1560350192,\n" +
                "  \"sys\": {\n" +
                "    \"type\": 3,\n" +
                "    \"id\": 2019346,\n" +
                "    \"message\": 0.0065,\n" +
                "    \"country\": \"JP\",\n" +
                "    \"sunrise\": 1560281377,\n" +
                "    \"sunset\": 1560333478\n" +
                "  },\n" +
                "  \"timezone\": 32400,\n" +
                "  \"id\": 1851632,\n" +
                "  \"name\": \"Shuzenji\",\n" +
                "  \"cod\": 200\n" +
                "}"
        mockResponse.setBody(body)
        mockWebServer.enqueue(mockResponse)

        val weatherInfoResponse = gson.fromJson(body, WeatherInfoResponse::class.java)
        val coord = Coord(22.0, 75.0)
        val response = remoteWeatherRepository.getWeatherInfoSync(coord)

        Assert.assertEquals(200, response?.code())
    }
}