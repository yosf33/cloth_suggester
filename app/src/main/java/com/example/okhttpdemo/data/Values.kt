package com.example.okhttpdemo.data

import com.google.gson.annotations.SerializedName

data class Values (
    @SerializedName("temperature")
    val temperature: Double
        )
