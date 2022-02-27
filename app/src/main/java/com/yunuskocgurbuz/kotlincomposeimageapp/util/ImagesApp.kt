package com.yunuskocgurbuz.kotlincomposeimageapp.util

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class ImagesApp : Application()
{
    override fun onCreate() {
        super.onCreate()
    }
}