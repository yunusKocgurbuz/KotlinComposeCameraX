package com.yunuskocgurbuz.kotlincomposeimageapp.repository

import com.yunuskocgurbuz.kotlincomposeimageapp.model.weather.WeatherModel
import com.yunuskocgurbuz.kotlincomposeimageapp.service.WeatherAPI
import com.yunuskocgurbuz.kotlincomposeimageapp.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class WeatherRepository @Inject constructor(
    private val api: WeatherAPI
){

    suspend fun getWeatherLocation(location: String): Resource<WeatherModel>{
        val response = try {
            api.getWeatherLoc(location)

        }catch (e: Exception){

            return Resource.Error("Error API connect!!")
        }


        return Resource.Success(response)
    }

}