package di

import AppController
import CountryController
import UsersController
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import com.github.tedblair2.db.AppDatabase
import datastore.createDataStore
import datastore.userSettings_filename
import org.koin.dsl.module
import java.io.File

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
        UsersController(get(),get(),get())
    }
    single {
        createDataStore {
            val file=File("datastore/$userSettings_filename")
            file.parentFile.mkdirs()
            file
        }
    }
}