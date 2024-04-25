package voyager.screenmodel

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.AppState
import service.UserService
import sqlDelight.events.UsersScreenActions
import sqlDelight.model.SqlUsersScreenState
import store.Dispatch
import store.MiddleWare
import store.Reducer
import store.Store

class SqlUsersScreenModel(
    private val store:Store,
    private val userService: UserService
):StateScreenModel<SqlUsersScreenState>(SqlUsersScreenState()) {

    private var dispatch:Dispatch?=null

    private val reducer:Reducer<SqlUsersScreenState> = {old, action ->
        when(action){
            is UsersScreenActions.UsersList->old.copy(users = action.users)
            else -> old
        }
    }

    private val appReducer:Reducer<AppState> ={ old , action ->
        old.copy(sqlUsersScreenState = reducer(old.sqlUsersScreenState,action))
    }

    private val middleWare:MiddleWare = {state, action, dispatch, next ->
        when(action){
            UsersScreenActions.GetUsers->{
                screenModelScope.launch {
                    userService.getUsers().collect{users->
                        dispatch(UsersScreenActions.UsersList(users))
                    }
                }
                action
            }
            is UsersScreenActions.DeleteUser->{
                screenModelScope.launch {
                    userService.deleteUser(action.id)
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
            launch {
                store.getCurrentState { dispatch=it }
                    .map { it.sqlUsersScreenState }
                    .collect{currentState->
                        mutableState.update {
                            currentState
                        }
                    }
            }
            launch {
                userService.getUsers().collect{users->
                    dispatch?.invoke(UsersScreenActions.UsersList(users))
                }
            }
        }
    }

    fun onEvent(action: UsersScreenActions){
        dispatch?.invoke(action)
    }
}