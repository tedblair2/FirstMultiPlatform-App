package model

data class AppState(
    val counterState: CounterState=CounterState(),
    val countryScreenState: CountryScreenState=CountryScreenState()
)
