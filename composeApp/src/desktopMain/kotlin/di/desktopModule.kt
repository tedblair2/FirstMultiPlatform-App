package di

import AppController
import CountryController
import UsersController
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.tedblair2.db.AppDatabase
import org.koin.dsl.module

val desktopModule= module {
    single {
        AppController(get())
    }
    single {
        CountryController(get(),get())
    }
    single<SqlDriver> {
        val driver:SqlDriver= JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
        AppDatabase.Schema.create(driver)
        driver
    }
    single {
        UsersController(get(),get())
    }
}