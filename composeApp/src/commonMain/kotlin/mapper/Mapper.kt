package mapper

import com.github.tedblair2.CountriesQuery
import com.github.tedblair2.CountryQuery
import model.DetailedCountry
import model.SimpleCountry

fun CountriesQuery.Country.toSimpleCountry(): SimpleCountry {
    return SimpleCountry(
        code = code,
        name = name,
        emoji = emoji,
        capital = capital ?: ""
    )
}

fun CountryQuery.Country.toDetailedCountry(): DetailedCountry {
    return DetailedCountry(
        code = code,
        name = name,
        currency = currency ?: "",
        capital = capital ?: "",
        languages = languages.joinToString(","){it.name},
        emoji = emoji,
        continent = continent.name
    )
}