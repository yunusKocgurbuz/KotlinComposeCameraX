package com.yunuskocgurbuz.kotlincomposeimageapp.service

import com.yunuskocgurbuz.kotlincomposeimageapp.model.weather.WeatherModel
import retrofit2.http.GET
import retrofit2.http.Path

interface WeatherAPI {



    //https://goweather.herokuapp.com/weather/van
    @GET("weather/{city_name}")
    suspend fun getWeatherLoc(
        @Path(value = "city_name", encoded = true) city_name: String,
    ): WeatherModel


}