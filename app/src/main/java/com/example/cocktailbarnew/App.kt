package com.example.cocktailbarnew

import android.app.Application
import com.example.cocktailbarnew.di.AppComponent
import com.example.cocktailbarnew.di.DaggerAppComponent

class App : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.factory().create(this)
    }
}