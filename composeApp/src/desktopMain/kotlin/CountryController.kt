
import event.CountriesAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import model.AppState
import model.CountryScreenState
import service.CountryService
import store.Dispatch
import store.MiddleWare
import store.Reducer
import store.Store

class CountryController(
    store: Store ,
    private val countryService: CountryService
) {
    private val controllerScope= CoroutineScope(Dispatchers.IO+ SupervisorJob())
    private var dispatch: Dispatch?=null
    val countryState=store.getCurrentState(subscriber = {dispatch=it})
        .map { it.countryScreenState }
        .stateIn(controllerScope, SharingStarted.WhileSubscribed(5000), CountryScreenState())

    private val reducer: Reducer<CountryScreenState> = { old , action ->
        when(action){
            is CountriesAction.CountriesResult->{
                old.copy(countries = action.list,isLoading = false)
            }
            is CountriesAction.SelectedCountry->{
                old.copy(selectedCountry = action.country)
            }
            CountriesAction.DismissDialog->{
                old.copy(selectedCountry = null)
            }
            else->old
        }
    }

    private val appStateReducer: Reducer<AppState> = { old , action ->
        old.copy(countryScreenState = reducer(old.countryScreenState,action))
    }

    private val middleWare: MiddleWare = { state , action , dispatch , next ->
        when(action){
            CountriesAction.GetCountries->{
                controllerScope.launch{
                    val countries=countryService.getCountries()
                    dispatch(CountriesAction.CountriesResult(countries))
                }
                action
            }
            is CountriesAction.SelectCountry->{
                controllerScope.launch {
                    val country=countryService.getCountry(action.code)
                    dispatch(CountriesAction.SelectedCountry(country))
                }
                action
            }
            else->next(state, action, dispatch)
        }
    }

    init {
        store.applyReducer(appStateReducer)
            .applyMiddleWare(middleWare)
        dispatch?.invoke(CountriesAction.GetCountries)
    }

    fun onEvent(action: CountriesAction){
        dispatch?.invoke(action)
    }

    fun onCleared(){
        dispatch=null
        controllerScope.cancel()
    }
}
