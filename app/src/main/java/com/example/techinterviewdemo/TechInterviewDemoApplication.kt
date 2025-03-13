// com.example.techinterviewdemo.TechInterviewDemoApplication.kt
package com.example.techinterviewdemo

import android.app.Application
import com.example.techinterviewdemo.di.AppContainer

class TechInterviewDemoApplication : Application() {

    lateinit var appContainer: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainer(this)
    }
}