package di

import com.apollographql.apollo3.ApolloClient
import com.github.tedblair2.db.AppDatabase
import org.koin.dsl.module
import service.AppStore
import service.CountryService
import service.CountryServiceImpl
import service.Store
import service.UserService
import service.UserServiceImpl

val commonModule= module {
    single<Store> {
        AppStore()
    }
    single<ApolloClient> {
        ApolloClient.Builder()
            .serverUrl("https://countries.trevorblades.com/graphql")
            .build()
    }
    single<CountryService>{
        CountryServiceImpl(get())
    }
    single<AppDatabase>{
        AppDatabase(get())
    }
    single<UserService>{
        UserServiceImpl(get())
    }
}