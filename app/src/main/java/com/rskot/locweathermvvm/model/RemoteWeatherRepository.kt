package com.rskot.locweathermvvm.model

import com.rskot.locweathermvvm.model.data_class.Coord
import com.rskot.locweathermvvm.model.data_class.WeatherData
import com.rskot.locweathermvvm.model.data_class.WeatherInfoResponse
import retrofit2.Response

/**
 * Fetches the data from remote server
 */
interface RemoteWeatherRepository {
    /**
     * Gets weather info from openweathermap API
     *
     * @param coord the location coordinates. if coord is null then stored coordinates will be used
     */
    fun getWeatherInfoSync(coord: Coord?): Response<WeatherInfoResponse>?

    /**
     * Gets weather info from openweathermap APIs
     * @param callback the API callback
     */
    fun getWeatherInfoAsync(callback: APIRequestListener<WeatherData?>)
}

interface APIRequestListener<T> {
    /**
     * API request is successful
     */
    fun onRequestSuccess(data: T)

    /**
     * API request is failed
     */
    fun onRequestFailed(errorMessage: String)
}