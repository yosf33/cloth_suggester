package com.example.okhttpdemo

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.okhttpdemo.data.ClothHelper
import com.example.okhttpdemo.data.Clothes
import com.example.okhttpdemo.data.NationalResponse
import com.example.okhttpdemo.databinding.ActivityMainBinding
import com.google.gson.Gson
import okhttp3.*
import java.io.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private val PREFS_NAME = "MyPrefsFile"
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var client: OkHttpClient
    private lateinit var binding: ActivityMainBinding
    private val getURL =
        "https://api.tomorrow.io/v4/timelines?location=28.0865950,30.7526881&fields=temperature&timesteps=current&units=metric&apikey=1lV97HRdMU867j2DkTLVns9tvNU6JUgd"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        client = OkHttpClient()
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        getDataFromApi()

        val savedData = sharedPreferences.getInt("id", 0)
        val savedDate = sharedPreferences.getString("date","")
        val newDate=getDate()
        if(isSameDate(savedDate,newDate)){
        if (savedData != 0) {
            val cloth = ClothHelper.clothesList.first { it.id == savedData }
            binding.image.setImageResource(cloth.imageId)
        }
        }

    }

    fun getDataFromApi() {
        val request = Request.Builder()
            .url(getURL)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                e.printStackTrace()
            }

            @Throws(IOException::class)
            override fun onResponse(call: Call, response: Response) {
                response.body?.string()?.let { jsonString ->
                    val result = Gson().fromJson(jsonString, NationalResponse::class.java)


                    runOnUiThread {
                        binding.textData.text =
                            result.data.timelines[0].intervals[0].values.temperature.toString()
                        val hotOrCold =
                            checkHotOrColdBasedOnTemprature(result.data.timelines[0].intervals[0].values.temperature)
                        val idOfSelectingOneCloth =
                            getListOfClothUsedOnSummerOrWinter(ClothHelper.clothesList, hotOrCold).random().id

                        val editor = sharedPreferences.edit()//like open file i use .edit
                        editor.putInt("id", idOfSelectingOneCloth)
                        editor.putString("date", getDate())
                        editor.apply()//close file

                    }
                }
            }
        })
    }

    fun checkHotOrColdBasedOnTemprature(temperature: Double): String {
        return when (temperature) {
            in 0.0..20.0 -> "cold"
            in 20.0..50.0 -> "warm"
            else -> {
                ""
            }
        }
    }

    fun getListOfClothUsedOnSummerOrWinter(list: List<Clothes>, type: String): List<Clothes> {
        val result = list.filter {
            it.type == type
        }
        return result
    }

    fun getDate(): String {
        val date = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("d M yyyy")
        val formattedDate = formatter.format(date)

        return formattedDate.toString()
    }

    fun isSameDate(savedDate: String?, newDate: String): Boolean {
        return when (savedDate?.compareTo(newDate)) {
            0 -> true
            else -> false
        }
    }
}