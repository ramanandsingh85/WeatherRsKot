package com.rskot.locweathermvvm.model.data_class


import com.google.gson.annotations.SerializedName

data class Clouds(
        @SerializedName("all")
        val all: Int = 0
)