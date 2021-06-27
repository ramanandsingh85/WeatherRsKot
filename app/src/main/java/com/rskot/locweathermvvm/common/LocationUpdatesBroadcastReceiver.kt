package com.rskot.locweathermvvm.common

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationResult
import com.rskot.locweathermvvm.model.WeatherRepositoryImpl
import com.rskot.locweathermvvm.model.data_class.Coord

private const val TAG = "LUBroadcastReceiver"

/**
 * Receiver for handling location updates.
 *
 * For apps targeting API level O and above
 * {@link android.app.PendingIntent#getBroadcast(Context, int, Intent, int)} should be used when
 * requesting location updates in the background.
 */
class LocationUpdatesBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(TAG, "onReceive() context:$context, intent:$intent")

        if (intent.action == ACTION_PROCESS_UPDATES) {

            // Checks for location availability changes.
            LocationAvailability.extractLocationAvailability(intent)?.let { locationAvailability ->
                if (!locationAvailability.isLocationAvailable) {
                    Log.d(TAG, "Location services are no longer available!")
                }
            }

            LocationResult.extractResult(intent)?.let { locationResult ->
                if(!locationResult.locations.isEmpty()) {
                    Log.d(TAG, "extractResult locationResult.locations.size=" + locationResult.locations.size)
                    //take the first location and start the work manager
                    val location = locationResult.locations.first()
                    val myLocationManager = MyLocationManager.getInstance(context = context)
                    val weatherRepository = WeatherRepositoryImpl.getInstance(context = context, myLocationManager = myLocationManager)
                    weatherRepository.setLastLocation(Coord(location.latitude, location.longitude))
                    WorkManagerUtils.startWeatherUpdates(context)

                    //Update active listeners waiting for location like user may be waiting for data to refresh in home screen
                    LocalBroadcastManager.getInstance(context).sendBroadcast(Intent(Constants.LIVE_LOCATION_UPDATES))
                }
            }
        }
    }

    companion object {
        const val ACTION_PROCESS_UPDATES = "com.rskot.locweathermvvm.common.LocationUpdatesBroadcastReceiver.action.PROCESS_UPDATES"
    }
}
