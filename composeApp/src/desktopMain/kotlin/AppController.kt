import event.CounterAction
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import model.AppState
import model.CounterState
import service.Dispatch
import service.MiddleWare
import service.Reducer
import service.Store

class AppController(
    store:Store
) {
    private val controllerScope= CoroutineScope(Dispatchers.IO+ SupervisorJob())
    private var dispatch:Dispatch?=null
    val counterState=store.getCurrentState(subscriber = {
        dispatch=it
    })
        .map { it.counterState }
        .stateIn(controllerScope, SharingStarted.WhileSubscribed(5000), CounterState())

    private val middleWare:MiddleWare = { state,action,dispatch,next->
        when(action){
            CounterAction.LoadData->{
                controllerScope.launch {
                    val data= fakeAsyncCall()
                    val newAction= CounterAction.DataLoaded(data)
                    dispatch(newAction)
                }
                action
            }
            else->next(state, action, dispatch)
        }
    }

    private val counterReducer:Reducer<CounterState> = { old , action->
        when(action){
            CounterAction.Decrement->old.copy(count = old.count-1)
            CounterAction.Increment->old.copy(count = old.count+1)
            is CounterAction.DataLoaded->old.copy(count = old.count+action.value)
            else -> old
        }
    }

    private val appReducer:Reducer<AppState> ={ old , action ->
        old.copy(counterState = counterReducer(old.counterState,action))
    }

    init {
        store.applyReducer(appReducer)
            .applyMiddleWare(middleWare)
        dispatch?.invoke(CounterAction.Init)
    }

    fun onEvent(counterAction: CounterAction){
        dispatch?.invoke(counterAction)
    }

    private suspend fun fakeAsyncCall():Int{
        delay(500)
        return 20
    }

    fun onCleared(){
        dispatch=null
        controllerScope.cancel()
    }
}