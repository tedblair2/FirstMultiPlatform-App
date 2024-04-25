package voyager.screenmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.AppState
import model.User
import service.UserService
import sqlDelight.events.AddUserEvents
import sqlDelight.model.AddUserScreenState
import store.Dispatch
import store.MiddleWare
import store.Reducer
import store.Store

class AddUserScreenModel(
    private val store: Store,
    private val userService: UserService
):StateScreenModel<AddUserScreenState>(AddUserScreenState()) {

    private var dispatch:Dispatch?=null

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
                screenModelScope.launch {
                    userService.addUser(user)
                }
                action
            }
            else->next(state, action, dispatch)
        }
    }

    init {
        initScreenModel()
    }

    fun initScreenModel(){
        store.applyReducer(appReducer)
            .applyMiddleWare(middleWare)
        screenModelScope.launch {
            store.getCurrentState { dispatch=it }
                .map { it.addUserScreenState }
                .collect{currentState->
                    mutableState.update { currentState }
                }
        }
    }

    fun onEvent(action:AddUserEvents){
        dispatch?.invoke(action)
    }
}