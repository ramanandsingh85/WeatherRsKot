package com.rskot.locweathermvvm.model

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rskot.locweathermvvm.common.Constants
import com.rskot.locweathermvvm.model.data_class.Coord
import com.rskot.locweathermvvm.model.data_class.WeatherData
import com.rskot.locweathermvvm.model.data_class.WeatherInfoResponse
import com.rskot.locweathermvvm.network.ApiInterface
import com.rskot.locweathermvvm.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

private const val TAG = "RWRepositoryImpl"

class RemoteWeatherRepositoryImpl(private val context: Context, private val weatherRepository: WeatherRepository): RemoteWeatherRepository {

    override fun getWeatherInfoSync(coord: Coord?): Response<WeatherInfoResponse>? {
        Log.d(TAG, "getWeatherInfoSync() received coord=" + coord.toString())
        weatherRepository.setLastLocation(coord)
        val storedCoords = weatherRepository.getLastLocation() ?: return null

        Log.d(TAG, "getWeatherInfoSync() storedCoords=$storedCoords")
        val apiInterface: ApiInterface = RetrofitClient.client.create(ApiInterface::class.java)
        val call: Call<WeatherInfoResponse> = apiInterface.getWeatherInfo(storedCoords!!.lat, storedCoords.lon)
        val  response = call.execute()

        if(response.isSuccessful) {
            //store for further usage
            weatherRepository.setLastCachedWeatherInfo(response.body()?.transformToWeatherData())
            //update the live receivers if any
            LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(Constants.LIVE_WEATHER_UPDATES))
        }
        return response
    }

    override fun getWeatherInfoAsync(callback: APIRequestListener<WeatherData?>) {
        Log.d(TAG, "getWeatherInfoSync() callback=$callback")
        val storedCoords = weatherRepository.getLastLocation()
        if(storedCoords == null) {
            callback.onRequestFailed("Current location is not set!")
            return
        }
        val cachedWeatherData = weatherRepository.getLastCachedWeatherInfo()
        if(cachedWeatherData != null) {
            callback.onRequestSuccess(cachedWeatherData)
            return
        }

        val apiInterface: ApiInterface = RetrofitClient.client.create(ApiInterface::class.java)
        val call: Call<WeatherInfoResponse> = apiInterface.getWeatherInfo(storedCoords!!.lat, storedCoords.lon)
        call.enqueue(object : Callback<WeatherInfoResponse> {

            // if retrofit network call success, this method will be triggered
            override fun onResponse(call: Call<WeatherInfoResponse>, response: Response<WeatherInfoResponse>) {
                val weatherResponse = response.body()
                if (weatherResponse != null)
                    callback.onRequestSuccess(weatherResponse.transformToWeatherData()) //let presenter know the weather information data
                else
                    callback.onRequestFailed(response.message()) //let presenter know about failure
            }

            // this method will be triggered if network call failed
            override fun onFailure(call: Call<WeatherInfoResponse>, t: Throwable) {
                callback.onRequestFailed(t.localizedMessage!!) //let presenter know about failure
            }
        })
    }
}