package com.yunuskocgurbuz.kotlincomposeimageapp.model.weather

data class WeatherModel(
    val description: String,
    val forecast: List<Forecast>,
    val temperature: String,
    val wind: String
)