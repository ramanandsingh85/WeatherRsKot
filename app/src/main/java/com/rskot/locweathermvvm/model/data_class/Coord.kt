package com.rskot.locweathermvvm.model.data_class


import com.google.gson.annotations.SerializedName

data class Coord(
        @SerializedName("lat")
        val lat: Double = 0.0,
        @SerializedName("lon")
        val lon: Double = 0.0
) {
        override fun toString(): String {
                return "Coord(lat=$lat, lon=$lon)"
        }
}