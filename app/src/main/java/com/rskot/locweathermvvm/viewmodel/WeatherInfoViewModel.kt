package com.rskot.locweathermvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.rskot.locweathermvvm.model.APIRequestListener
import com.rskot.locweathermvvm.model.RemoteWeatherRepository
import com.rskot.locweathermvvm.model.WeatherRepository
import com.rskot.locweathermvvm.model.data_class.WeatherData

class WeatherInfoViewModel : ViewModel() {
    var weatherRepository: WeatherRepository? = null
    var remoteWeatherRepository: RemoteWeatherRepository? = null

    /**
     * In our project, for sake for simplicity we used different LiveData for success and failure.
     * But it's not the only way. We can use a wrapper data class to implement success and failure
     * both using a single LiveData. Another good approach may be handle errors in BaseActivity.
     * For this project our objective is only understand about MVVM. So we made it easy to understand.
     */
    val _weatherInfoLiveData = MutableLiveData<WeatherData>()
    val weatherInfoLiveData: LiveData<WeatherData> get() = _weatherInfoLiveData

    val _weatherInfoFailureLiveData = MutableLiveData<String>()
    val weatherInfoFailureLiveData: LiveData<String> get() = _weatherInfoFailureLiveData

    val _progressBarLiveData = MutableLiveData<Boolean>()
    val progressBarLiveData: LiveData<Boolean> get() = _progressBarLiveData

    /**
     * Gets the stored weather info if any
     */
    fun getCachedWeatherInfo() {
        _progressBarLiveData.postValue(true) // PUSH data to LiveData object to show progress bar

        remoteWeatherRepository?.getWeatherInfoAsync(object :
            APIRequestListener<WeatherData?> {
            override fun onRequestSuccess(data: WeatherData?) {
                _progressBarLiveData.postValue(false) // PUSH data to LiveData object to hide progress bar

                // After applying business logic and data manipulation, we push data to show on UI
                _weatherInfoLiveData.postValue(data) // PUSH data to LiveData object
            }

            override fun onRequestFailed(errorMessage: String) {
                _progressBarLiveData.postValue(false) // hide progress bar
                _weatherInfoFailureLiveData.postValue(errorMessage) // PUSH error message to LiveData object
            }
        })
    }

    fun startLocationUpdates() = weatherRepository?.startLocationUpdates()

    fun stopLocationUpdates() = weatherRepository?.stopLocationUpdates()
}