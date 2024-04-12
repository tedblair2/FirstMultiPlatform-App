package service

import com.apollographql.apollo3.ApolloClient
import com.github.tedblair2.CountriesQuery
import com.github.tedblair2.CountryQuery
import mapper.toDetailedCountry
import mapper.toSimpleCountry
import model.DetailedCountry
import model.SimpleCountry

class CountryServiceImpl(
    private val apolloClient: ApolloClient
) : CountryService {

    override suspend fun getCountries(): List<SimpleCountry> {
        return apolloClient
            .query(CountriesQuery())
            .execute()
            .data
            ?.countries
            ?.map { it.toSimpleCountry() }
            ?.sortedBy { it.name }
            ?: emptyList()
    }

    override suspend fun getCountry(code: String): DetailedCountry? {
        return apolloClient
            .query(CountryQuery(code))
            .execute()
            .data
            ?.country
            ?.toDetailedCountry()
    }
}