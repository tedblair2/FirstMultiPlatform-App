package model

data class CountryScreenState(
    val countries:List<SimpleCountry> = emptyList(),
    val isLoading:Boolean=true,
    val selectedCountry: DetailedCountry?=null
)