package com.github.tedblair2.di

import com.github.tedblair2.viewmodel.AppViewModel
import com.github.tedblair2.viewmodel.CountriesViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val androidModule= module {
    viewModel {
        AppViewModel(get())
    }
    viewModel{
        CountriesViewModel(get(),get())
    }
}