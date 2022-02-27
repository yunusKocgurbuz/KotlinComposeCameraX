package com.yunuskocgurbuz.kotlincomposeimageapp.dependencyinjection

import com.yunuskocgurbuz.kotlincomposeimageapp.repository.WeatherRepository
import com.yunuskocgurbuz.kotlincomposeimageapp.service.WeatherAPI
import com.yunuskocgurbuz.kotlincomposeimageapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWeatherRepository(
        api: WeatherAPI
    ) = WeatherRepository(api)

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(WeatherAPI::class.java)
    }
}