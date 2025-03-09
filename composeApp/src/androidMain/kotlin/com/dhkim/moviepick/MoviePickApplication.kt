package com.dhkim.moviepick

import android.app.Application
import com.dhkim.di.initKoin
import org.koin.android.ext.koin.androidContext

class MoviePickApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MoviePickApplication)
        }
    }
}