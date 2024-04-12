package service

import model.DetailedCountry
import model.SimpleCountry

interface CountryService {
    suspend fun getCountries():List<SimpleCountry>
    suspend fun getCountry(code:String):DetailedCountry?
}