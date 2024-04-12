package com.github.tedblair2

import android.app.Application
import com.github.tedblair2.di.androidModule
import di.commonModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class AndroidApplication: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidLogger()
            androidContext(this@AndroidApplication)
            modules(commonModule, androidModule)
        }
    }
}