package com.example.kotlinware

import android.app.Application
import com.example.kotlinware.data.AppContainer
import com.example.kotlinware.data.AppDataContainer

class KotlinWareApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}