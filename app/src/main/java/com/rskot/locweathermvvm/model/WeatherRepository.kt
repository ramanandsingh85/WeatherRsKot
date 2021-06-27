package com.rskot.locweathermvvm.model

import androidx.annotation.MainThread
import com.rskot.locweathermvvm.model.data_class.Coord
import com.rskot.locweathermvvm.model.data_class.WeatherData

/**
 * Stores weather data and location data
 */
interface WeatherRepository {
    /**
     * Stores weather info in shared preferences
     *
     * @param weatherData the weather data
     */
    fun setLastCachedWeatherInfo(weatherData: WeatherData?)

    /**
     * Gets stored weather info from shared preferences
     *
     * @return WeatherData if location is previously detected and stored, null otherwise
     */
    fun getLastCachedWeatherInfo(): WeatherData?

    /**
     * Stores location coordinates in shared preferences
     *
     * @param coord the location coordinates. if coord is null then it will not be saved and hence used stored ones for further calls.
     */
    fun setLastLocation(coord: Coord?)

    /**
     * Gets stored location coordinates from shared preferences
     *
     * @return Coord the Coord object if non null value stored already null otherwise
     */
    fun getLastLocation(): Coord?

    /**
     * Subscribes to location updates.
     */
    @MainThread
    fun startLocationUpdates()

    /**
     * Un-subscribes from location updates.
     */
    @MainThread
    fun stopLocationUpdates()
}