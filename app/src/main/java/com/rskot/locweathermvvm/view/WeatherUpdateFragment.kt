package com.rskot.locweathermvvm.view

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.bumptech.glide.Glide
import com.rskot.locweathermvvm.common.Constants
import com.rskot.locweathermvvm.common.MyLocationManager
import com.rskot.locweathermvvm.model.RemoteWeatherRepositoryImpl
import com.rskot.locweathermvvm.model.WeatherRepositoryImpl
import com.rskot.locweathermvvm.model.data_class.WeatherData
import com.rskot.locweathermvvm.viewmodel.WeatherInfoViewModel
import com.rskot.locweathermvvm.R
import kotlinx.android.synthetic.main.fragment_location_update.*
import kotlinx.android.synthetic.main.layout_sunrise_sunset.*
import kotlinx.android.synthetic.main.layout_weather_additional_info.*
import kotlinx.android.synthetic.main.layout_weather_basic_info.*

private const val TAG = "WeatherUpdateFragment"

/**
 * Displays weather information via PendingIntent and BroadcastReceiver after permissions are approved.
 */
class WeatherUpdateFragment : Fragment() {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(WeatherInfoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        context?.apply {
            // initialize model. (I know we should not initialize model in View. But for simplicity...)
            val myLocationManager = MyLocationManager.getInstance(context = this)
            // initialize ViewModel
            val weatherRepository = WeatherRepositoryImpl.getInstance(context = this, myLocationManager = myLocationManager)
            viewModel.weatherRepository = weatherRepository
            viewModel.remoteWeatherRepository = RemoteWeatherRepositoryImpl(this, weatherRepository)
        }

        return inflater.inflate(R.layout.fragment_location_update, container, false);
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // set LiveData and View click listeners before the call for data fetching
        setLiveDataListeners()

        /**
         * Fetch city list when Activity open.
         * It's not a very good way that, passing model in every methods of ViewModel. For the sake
         * of simplicity I did so. In real production level App, we can inject out model to ViewModel
         * as a parameter by any dependency injection library like Dagger.
         */
        viewModel.startLocationUpdates()
        viewModel.getCachedWeatherInfo()
    }

    private fun setLiveDataListeners() {
        /*
         * ProgressBar visibility will be handled by this LiveData. ViewModel decides when Activity
         * should show ProgressBar and when hide.
         *
         * Here I've used lambda expression to implement Observer interface in second parameter.
         */
        viewModel.progressBarLiveData.observe(this, Observer { isShowLoader ->
            if (isShowLoader)
                progressBar.visibility = View.VISIBLE
            else
                progressBar.visibility = View.GONE
        })

        /*
         * This method will be triggered when ViewModel successfully receive WeatherData from our
         * data source (I mean Model). Activity just observing (subscribing) this LiveData for showing
         * weather information on UI. ViewModel receives Weather data API response from Model via
         * Callback method of Model. Then ViewModel apply some business logic and manipulate data.
         * Finally ViewModel PUSH WeatherData to `weatherInfoLiveData`. After PUSHING into it, below
         * method triggered instantly! Then we set the data on UI.
         *
         * Here I've used lambda expression to implement Observer interface in second parameter.
         *
         * Used to fetch stored data
         */
        viewModel.weatherInfoLiveData?.observe(this, Observer { weatherData ->
            setWeatherInfo(weatherData)
        })

        /*
         * If ViewModel faces any error during Weather Info fetching API call by Model, then PUSH the
         * error message into `weatherInfoFailureLiveData`. After that, this method will be triggered.
         * Then we will hide the output view and show error message on UI.
         *
         * Here I've used lambda expression to implement Observer interface in second parameter.
         */
        viewModel.weatherInfoFailureLiveData.observe(this, Observer { errorMessage ->
            outputGroup.visibility = View.GONE
            errorMessageTextView.visibility = View.VISIBLE
            errorMessageTextView.text = errorMessage
        })
    }

    /**
     * Method visibility should be private, temporary making is public for testing purpose
     * TODO: make is private after testing
     */
    fun setWeatherInfo(weatherData: WeatherData) {
        outputGroup.visibility = View.VISIBLE
        errorMessageTextView.visibility = View.GONE

        dateTimeTextView?.text = weatherData.dateTime
        temperatureTextView?.text = weatherData.temperature
        cityCountryTextView?.text = weatherData.cityAndCountry
        Glide.with(this).load(weatherData.weatherConditionIconUrl).into(weatherConditionImageView)
        weatherConditionTextView?.text = weatherData.weatherConditionIconDescription

        humidityValueTextView?.text = weatherData.humidity
        pressureValueTextView?.text = weatherData.pressure
        visibilityValueTextView?.text = weatherData.visibility

        sunriseTimeTextView?.text = weatherData.sunrise
        sunsetTimeTextView?.text = weatherData.sunset
    }

    /*
    Live weather update receiver
     */
    private val weatherUpdateReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {
            // Data is already stored in cache so refresh it
            viewModel.getCachedWeatherInfo()
        }
    }

    override fun onStart() {
        super.onStart()
        //Register to receive weather updates.
        // We are registering an observer (weatherUpdateReceiver) to receive Intents
        // with actions named Constants.LIVE_WEATHER_UPDATES.
        context?.let {
            val intentFilter = IntentFilter(Constants.LIVE_WEATHER_UPDATES).apply {
                addAction(Constants.LIVE_LOCATION_UPDATES)
            }
            LocalBroadcastManager.getInstance(it).registerReceiver(weatherUpdateReceiver, intentFilter)
        };
    }

    override fun onStop() {
        super.onStop()
        context?.let {
            LocalBroadcastManager.getInstance(it).unregisterReceiver(weatherUpdateReceiver)
        };
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface Callbacks {
        fun requestFineLocationPermission()
        fun requestBackgroundLocationPermission()
    }

    companion object {
        fun newInstance() = WeatherUpdateFragment()
    }
}
