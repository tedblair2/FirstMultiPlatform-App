package event

import model.DetailedCountry
import model.SimpleCountry
import service.Action

sealed interface CountriesAction: Action {
    data object GetCountries:CountriesAction
    data class CountriesResult(val list: List<SimpleCountry>):CountriesAction
    data class SelectCountry(val code:String):CountriesAction
    data class SelectedCountry(val country: DetailedCountry?=null):CountriesAction
    data object DismissDialog:CountriesAction
}