package precompose.viewmodel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import model.AppState
import model.User
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import service.UserService
import sqlDelight.events.AddUserEvents
import sqlDelight.model.AddUserScreenState
import store.Dispatch
import store.MiddleWare
import store.Reducer
import store.Store

class AddUserViewModel(
    store: Store,
    private val userService: UserService
):ViewModel() {

    private var dispatch:Dispatch?=null

    val addUserScreenState=store.getCurrentState { dispatch=it }
        .map { it.addUserScreenState }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), AddUserScreenState())

    private val reducer:Reducer<AddUserScreenState> = {old, action ->
        when(action){
            is AddUserEvents.UpdateName->old.copy(name = action.name)
            is AddUserEvents.UpdateAddress->old.copy(address = action.address)
            is AddUserEvents.UpdateAge->old.copy(age = action.age)
            AddUserEvents.ResetValues->old.copy(name = "", age = "", address = "")
            else->old
        }
    }

    private val appReducer:Reducer<AppState> = { old , action ->
        old.copy(addUserScreenState = reducer(old.addUserScreenState,action))
    }

    private val middleWare:MiddleWare = {state, action, dispatch, next ->
        when(action){
            AddUserEvents.SaveUserToDb->{
                val name=state.addUserScreenState.name
                val address=state.addUserScreenState.address
                val age=state.addUserScreenState.age
                val user= User(
                    name = name,
                    address = address,
                    age = age.toInt()
                )
                viewModelScope.launch {
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