package com.rskot.locweathermvvm.model

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.rskot.locweathermvvm.common.MyLocationManager
import com.rskot.locweathermvvm.model.data_class.Coord
import com.rskot.locweathermvvm.model.data_class.WeatherData

private const val TAG = "RWRepositoryImpl"

/**
 * Stores weather data and location data
 */
class WeatherRepositoryImpl private constructor(private val context: Context, private val myLocationManager: MyLocationManager): WeatherRepository {
    private val sharedPreferences by lazy {
        context.getSharedPreferences(TAG, Context.MODE_PRIVATE)
    }
    private val gson by lazy { Gson() }

    override fun setLastCachedWeatherInfo(weatherInfo: WeatherData?) {
        Log.d(TAG, "setLastCachedWeatherInfo() weatherInfo=" + weatherInfo.toString())
        //Update weather data only if the data is received
        weatherInfo?.let {
            val lastCachedWeatherInfo = gson.toJson(weatherInfo)
            sharedPreferences.edit().putString("lastCachedWeatherInfo", lastCachedWeatherInfo)
                    .commit()
            Log.d(TAG, "setLastCachedWeatherInfo() updated last Cached Weather Info")
        }
    }

    override fun getLastCachedWeatherInfo(): WeatherData? {
        val lastCachedWeatherInfo = sharedPreferences.getString("lastCachedWeatherInfo", null);
        try {
            Log.d(TAG, "setLastCachedWeatherInfo() lastCachedWeatherInfo=$lastCachedWeatherInfo")
            return gson.fromJson(lastCachedWeatherInfo, WeatherData::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        Log.d(TAG, "getLastCachedWeatherInfo() returned null")
        return null
    }

    override fun setLastLocation(coord: Coord?) {
        Log.d(TAG, "setLastLocation() coord=" + coord.toString())
        //if location not detected then keep older ones
        coord?.let {
            val coordinates = gson.toJson(coord)
            sharedPreferences.edit().putString("coordinates", coordinates).commit()
            Log.d(TAG, "setLastLocation() updated last coordinates")
        }
    }

    override fun getLastLocation(): Coord? {
        val coordinates = sharedPreferences.getString("coordinates", null);
        try {
            Log.d(TAG, "getLastLocation() coordinates=$coordinates")
            return gson.fromJson(coordinates, Coord::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        Log.d(TAG, "getLastLocation() returned null")
        return null
    }

    @MainThread
    override fun startLocationUpdates() = myLocationManager.startLocationUpdates()

    @MainThread
    override fun stopLocationUpdates() = myLocationManager.stopLocationUpdates()

    companion object {
        @Volatile private var INSTANCE: WeatherRepositoryImpl? = null

        fun getInstance(context: Context, myLocationManager: MyLocationManager): WeatherRepositoryImpl {
            return INSTANCE
                ?: synchronized(this) {
                    INSTANCE
                        ?: WeatherRepositoryImpl(
                            context,
                            myLocationManager
                        )
                            .also { INSTANCE = it }
                }
        }
    }
}