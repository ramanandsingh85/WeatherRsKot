# Weather RSKot

This is a simple weather app that displays the current weather forecast using the OpenWeatherMap free weather API. The app usage negligible battery still keeps users updated with current weather information.

#### Components Used
Kotlin, MVVM, Retrofit, WorkManager, BackgroundLocation, BroadcastReceiver, LocalBroadcastReceiver, Activity, Fragments, ViewModel, Repository, UnitTests, InstrumentationTest

## How to compile

1. Clone the code in the local machine.

```bash
git clone https://github.com/ramanandsingh85/WeatherRsKot.git
```
2. Define openweathermap required credentials in local.properties as following:

```bash
#openweathermap Base URL https://openweathermap.org/api
base_url=http://api.openweathermap.org/data/2.5/

#App ID of Open Weather API https://home.openweathermap.org/api_keys
app_id=YOUR_OWN_APP_ID
```
3. Sync, build, and run the app.

## Design Decisions and Code Structure

1. API key is read through the property file so that it's always secure and stays away from server commits.
2. Location is read through pending intent and broadcast receiver so that keeps saving in the background. 
3. Work manager is used to refreshing weather information every two hours. This is the latest offering from the jetpack. It is reliable and offers many customization options like network usage.
4. Wellknown Retrofit library is used to make API calls. Code produced by Retrofit is readable and well-formatted.
5. Weather data once fetched is saved in the cache. As soon as the user opens the app, this information is loaded right away.
6. In case the user is on the weather page and the latest information is fetched from the server, the local broadcast helps to show current weather information right away.
7. Even if the user turn off the location access, still the app fetches last stored location's weather information.

## Future Enhancements
1. Need to use dagger so that dependency injection benefits can be made available.
2. Need to write test cases so that code coverage is near equal to 100%.
3. Need to employ an intelligent algorithm for location usage so that it is always accurate whether the user is moving or stand still.
4 Need to optimize server calls in a way to reduce if a user is a standstill for some time.
5. Need to have a notification about weather updates to keep the user engaged and benefitted from current weather condition alerts.

## Contributing
Pull requests are welcome. For major changes, please open an issue first to discuss what you would like to change.

Please make sure to update tests as appropriate.