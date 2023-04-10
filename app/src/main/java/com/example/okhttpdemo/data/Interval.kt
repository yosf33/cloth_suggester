package com.example.okhttpdemo.data

import com.google.gson.annotations.SerializedName

data class Interval(
        @SerializedName("startTime")
        val startTime: String,

        @SerializedName("values")
        val values: Values

)
