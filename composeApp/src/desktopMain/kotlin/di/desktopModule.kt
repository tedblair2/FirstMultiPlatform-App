package di

import AppController
import CountryController
import org.koin.dsl.module

val desktopModule= module {
    single {
        AppController(get())
    }
    single {
        CountryController(get(),get())
    }
}