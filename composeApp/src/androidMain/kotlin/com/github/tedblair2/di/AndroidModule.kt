package com.github.tedblair2.di

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.github.tedblair2.db.AppDatabase
import com.github.tedblair2.viewmodel.AppViewModel
import com.github.tedblair2.viewmodel.CountriesViewModel
import com.github.tedblair2.viewmodel.UsersViewModel
import datastore.createDataStore
import datastore.userSettings_filename
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import java.io.File

val androidModule= module {
    viewModel {
        AppViewModel(get())
    }
    viewModel{
        CountriesViewModel(get(),get())
    }
    single<SqlDriver>{
        AndroidSqliteDriver(AppDatabase.Schema,androidApplication().applicationContext,"multiplatform.db")
    }
    viewModel {
        UsersViewModel(get(),get())
    }
    single {
        val context=androidApplication().applicationContext
        createDataStore {
            File("${context.cacheDir.path}/$userSettings_filename")
        }
    }
}