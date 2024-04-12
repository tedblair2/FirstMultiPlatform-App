package di

import com.apollographql.apollo3.ApolloClient
import org.koin.dsl.module
import service.AppStore
import service.CountryService
import service.CountryServiceImpl
import service.Store

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
}