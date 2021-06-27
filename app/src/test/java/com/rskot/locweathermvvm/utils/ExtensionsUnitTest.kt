package com.rskot.locweathermvvm.utils

import org.junit.Assert
import org.junit.Test

class ExtensionsUnitTest {

    @Test
    fun unixTimestampToDateTimeString_isCorrect() {
        val timestamp = 1624724832
        Assert.assertEquals("26 Jun, 2021 - 09:57 PM", timestamp.unixTimestampToDateTimeString())

        val timestamp2 = 1524724832
        Assert.assertEquals("26 Apr, 2018 - 12:10 PM", timestamp2.unixTimestampToDateTimeString())
    }

    @Test
    fun unixTimestampToTimeString_isCorrect() {
        val timestamp = 1624724832
        Assert.assertEquals("09:57 PM", timestamp.unixTimestampToTimeString())

        val timestamp2 = 1624724832
        Assert.assertEquals("09:57 PM", timestamp2.unixTimestampToTimeString())
    }

    @Test
    fun kelvinToCelsius_isCorrect() {
        val k1 = 0.0
        Assert.assertEquals(-273, k1.kelvinToCelsius())

        val k2 = 310.15
        Assert.assertEquals(37, k2.kelvinToCelsius())

        val k3 = 373.15
        Assert.assertEquals(100, k3.kelvinToCelsius())
    }
}