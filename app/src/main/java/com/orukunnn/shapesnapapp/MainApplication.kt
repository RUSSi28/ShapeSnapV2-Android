package com.orukunnn.shapesnapapp

import android.app.Application
import com.orukunnn.shapesnapapp.di.modules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
           androidLogger()
           androidContext(this@MainApplication)
           modules(modules)
        }
    }
}