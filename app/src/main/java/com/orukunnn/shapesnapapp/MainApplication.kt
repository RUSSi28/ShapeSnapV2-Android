package com.orukunnn.shapesnapapp

import android.app.Application
import com.google.android.gms.ads.MobileAds
import com.orukunnn.shapesnapapp.di.modules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class MainApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        // Mobile Ads SDK の初期化
        MobileAds.initialize(this)

        startKoin {
           androidLogger()
           androidContext(this@MainApplication)
           modules(modules)
        }
    }
}