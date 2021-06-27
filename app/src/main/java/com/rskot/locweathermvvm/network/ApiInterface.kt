package com.rskot.locweathermvvm.network

import com.rskot.locweathermvvm.model.data_class.WeatherInfoResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("weather")
    fun getWeatherInfo(@Query("lat") latitude: Double, @Query("lon") longitude: Double): Call<WeatherInfoResponse>
}