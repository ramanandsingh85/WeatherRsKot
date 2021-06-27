package com.rskot.locweathermvvm.view

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.rskot.locweathermvvm.R
import com.rskot.locweathermvvm.model.data_class.WeatherData
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class WeatherUpdatesFragmentTest {

    private lateinit var scenario: FragmentScenario<WeatherUpdateFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer()
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun setWeatherInfo_isCorrect() {
        scenario.onFragment(object : FragmentScenario.FragmentAction<WeatherUpdateFragment> {
            override fun perform(fragment: WeatherUpdateFragment) {
                //we can have use mock objects from mockito here
                var weatherData = WeatherData(cityAndCountry = "Indore, IN")
                fragment.setWeatherInfo(weatherData)
            }
        })
        onView(withId(R.id.cityCountryTextView)).check(matches(withText("Indore, IN")))
    }
}