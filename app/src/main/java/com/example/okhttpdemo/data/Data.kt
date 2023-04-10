package com.example.okhttpdemo.data

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("timelines")
    val timelines: List<Timeline>
)
