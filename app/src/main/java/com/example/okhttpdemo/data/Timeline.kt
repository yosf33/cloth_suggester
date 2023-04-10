package com.example.okhttpdemo.data

import com.google.gson.annotations.SerializedName

data class Timeline(
    @SerializedName("timestep")
    val timeStep: String,

    @SerializedName("endTime")
    val endTime: String,

    @SerializedName("startTime")
    val startTime: String,

    @SerializedName("intervals")
    val intervals: List<Interval>
)
