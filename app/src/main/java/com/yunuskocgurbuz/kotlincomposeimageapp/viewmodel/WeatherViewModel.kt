package com.yunuskocgurbuz.kotlincomposeimageapp.viewmodel

import androidx.lifecycle.ViewModel
import com.yunuskocgurbuz.kotlincomposeimageapp.model.weather.WeatherModel
import com.yunuskocgurbuz.kotlincomposeimageapp.repository.WeatherRepository
import com.yunuskocgurbuz.kotlincomposeimageapp.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
): ViewModel() {

    suspend fun getWeatherForLocation(location: String): Resource<WeatherModel> {
        return repository.getWeatherLocation(location)
    }

}