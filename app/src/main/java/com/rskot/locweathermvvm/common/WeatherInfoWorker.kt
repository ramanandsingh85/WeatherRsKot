package com.rskot.locweathermvvm.common

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.rskot.locweathermvvm.model.RemoteWeatherRepositoryImpl
import com.rskot.locweathermvvm.model.WeatherRepositoryImpl
import com.rskot.locweathermvvm.model.data_class.Coord

private const val TAG = "WeatherInfoWorker"

/**
 * Schedules weather updates
 */
class WeatherInfoWorker(context: Context, workerParams: WorkerParameters) : Worker(context, workerParams) {

    override fun doWork(): Result {
        Log.d(TAG, "doWork() started at " + System.currentTimeMillis())
        //Initial required variables and repository, later these can be injected through dagger
        val myLocationManager = MyLocationManager.getInstance(context = applicationContext)
        val weatherRepository = WeatherRepositoryImpl.getInstance(context = applicationContext, myLocationManager = myLocationManager)
        val remoteWeatherRepository = RemoteWeatherRepositoryImpl(applicationContext, weatherRepository)

        val coord: Coord? = myLocationManager.getLastKnownLocation()?.let {
            Coord(it.latitude, it.longitude)
        }
        val response = remoteWeatherRepository.getWeatherInfoSync(coord)
        Log.d(TAG, "doWork() response received at " + System.currentTimeMillis())

        if (response != null) {
            if (response.isSuccessful) {
                return Result.success()
            } else {
                if (response.code() in (500..599)) {
                    // try again if there is a server error
                    return Result.retry()
                }
                return Result.failure()
            }
        }
        return Result.success()
    }
}
