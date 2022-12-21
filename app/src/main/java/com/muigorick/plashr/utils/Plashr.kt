package com.muigorick.plashr.utils

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class Plashr : Application() {

    companion object {
        lateinit var instance: Plashr
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    fun getInstance(): Plashr {
        return instance
    }

}