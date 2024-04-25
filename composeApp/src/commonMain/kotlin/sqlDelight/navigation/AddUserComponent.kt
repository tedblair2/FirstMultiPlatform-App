package sqlDelight.navigation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import model.AppState
import model.User
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import service.AppCoroutineContext
import service.UserService
import sqlDelight.events.AddUserEvents
import sqlDelight.model.AddUserScreenState
import store.Dispatch
import store.MiddleWare
import store.Reducer
import store.Store

class AddUserComponent(
    componentContext: ComponentContext,
    private val onNavigateUp:()->Unit
):ComponentContext by componentContext,KoinComponent {

    private val store: Store by inject<Store>()
    private val appCoroutineContext: AppCoroutineContext by inject<AppCoroutineContext>()
    private val userService: UserService by inject<UserService>()
    private val componentScope= CoroutineScope(appCoroutineContext.io+ SupervisorJob())
    private var dispatch:Dispatch?=null

    val state=store.getCurrentState { dispatch=it }
        .map { it.addUserScreenState }
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000), AddUserScreenState())

    private val reducer:Reducer<AddUserScreenState> = {old, action ->
        when(action){
            is AddUserEvents.UpdateName->old.copy(name = action.name)
            is AddUserEvents.UpdateAddress->old.copy(address = action.address)
            is AddUserEvents.UpdateAge->old.copy(age = action.age)
            AddUserEvents.ResetValues->old.copy(name = "", age = "", address = "")
            else->old
        }
    }

    private val appReducer:Reducer<AppState> = {old, action ->
        old.copy(addUserScreenState = reducer(old.addUserScreenState,action))
    }

    private val middleWare:MiddleWare = {state, action, dispatch, next ->
        when(action){
            AddUserEvents.SaveUserToDb->{
                componentScope.launch {
                    val name=state.addUserScreenState.name
                    val address=state.addUserScreenState.address
                    val age=state.addUserScreenState.age
                    val user= User(
                        name = name,
                        address = address,
                        age = age.toInt()
                    )
                    userService.addUser(user)
                }
                action
            }
            else->next(state, action, dispatch)
        }
    }

    init {
        store.applyReducer(appReducer)
            .applyMiddleWare(middleWare)
    }

    fun onEvent(action:AddUserEvents){
        dispatch?.invoke(action)
    }
}