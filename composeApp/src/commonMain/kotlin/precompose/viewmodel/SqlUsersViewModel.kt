package precompose.viewmodel

import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import model.AppState
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope
import service.UserService
import sqlDelight.events.UsersScreenActions
import sqlDelight.model.SqlUsersScreenState
import store.Dispatch
import store.MiddleWare
import store.Reducer
import store.Store

class SqlUsersViewModel(
    store: Store,
    private val userService: UserService
):ViewModel() {

    private var dispatch:Dispatch?=null

    val sqlUsersScreenState=store.getCurrentState { dispatch=it }
        .map { it.sqlUsersScreenState }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), SqlUsersScreenState())

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
                viewModelScope.launch {
                    userService.getUsers().collect{users->
                        dispatch(UsersScreenActions.UsersList(users))
                    }
                }
                action
            }
            is UsersScreenActions.DeleteUser->{
                viewModelScope.launch {
                    userService.deleteUser(action.id)
                }
                action
            }
            else->next(state, action, dispatch)
        }
    }

    init {
        store.applyReducer(appReducer)
            .applyMiddleWare(middleWare)

        viewModelScope.launch {
            userService.getUsers().collect{users->
                dispatch?.invoke(UsersScreenActions.UsersList(users))
            }
        }
    }

    fun onEvent(action: UsersScreenActions){
        dispatch?.invoke(action)
    }
}