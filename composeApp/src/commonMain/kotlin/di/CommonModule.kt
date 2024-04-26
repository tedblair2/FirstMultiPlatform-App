package di

import com.apollographql.apollo3.ApolloClient
import com.github.tedblair2.db.AppDatabase
import datastore.UserSettingsScreenModel
import datastore.UserSettingsService
import datastore.UserSettingsServiceImpl
import org.koin.dsl.module
import precompose.viewmodel.AddUserViewModel
import precompose.viewmodel.SqlUsersViewModel
import service.AppCoroutineContext
import service.AppCoroutineContextImpl
import service.CountryService
import service.CountryServiceImpl
import service.UserService
import service.UserServiceImpl
import store.AppStore
import store.Store
import voyager.screenmodel.AddUserScreenModel
import voyager.screenmodel.SqlUsersScreenModel

val commonModule= module {
    single<Store> {
        AppStore()
    }
    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl("https://countries.trevorblades.com/graphql")
            .build()
    }
    single<AppCoroutineContext>{
        AppCoroutineContextImpl()
    }
    single<CountryService>{
        CountryServiceImpl(get())
    }
    single<AppDatabase>{
        AppDatabase(get())
    }
    single<UserService>{
        UserServiceImpl(get(),get())
    }
    factory {
        SqlUsersViewModel(get(),get())
    }
    factory {
        AddUserViewModel(get(),get())
    }
    factory {
        SqlUsersScreenModel(get(),get())
    }
    factory {
        AddUserScreenModel(get(),get())
    }
    single<UserSettingsService> {
        UserSettingsServiceImpl(get())
    }
    factory {
        UserSettingsScreenModel(get())
    }
}