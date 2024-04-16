package com.github.tedblair2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import event.UsersAction
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import model.AppState
import model.User
import model.UsersScreenState
import service.Dispatch
import service.MiddleWare
import service.Reducer
import service.Store
import service.UserService

class UsersViewModel(
    store: Store,
    private val userService: UserService
):ViewModel() {

    private var dispatch:Dispatch?=null
    val usersState=store.getCurrentState { dispatch=it }
        .map { it.usersScreenState }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UsersScreenState())

    private val usersReducer:Reducer<UsersScreenState> = {old, action ->
        when(action){
            is UsersAction.UsersList->{
                old.copy(users = action.users)
            }
            is UsersAction.UpdateName->{
                old.copy(name = action.name)
            }
            is UsersAction.UpdateAddress->{
                old.copy(address = action.address)
            }
            is UsersAction.UpdateAge->{
                old.copy(age = action.age)
            }
            UsersAction.ResetValues->{
                old.copy(name = "", address = "", age = "")
            }
            is UsersAction.SelectedUser->{
                old.copy(selectedUser = action.user)
            }
            UsersAction.DismissDialog->{
                old.copy(selectedUser = null)
            }
            else->old
        }
    }

    private val appReducer:Reducer<AppState> = {old, action ->
        old.copy(usersScreenState = usersReducer(old.usersScreenState,action))
    }

    private val middleWare:MiddleWare = {state, action, dispatch, next ->
        when(action){
            UsersAction.GetUsers->{
                viewModelScope.launch {
                    userService.getUsers().collect{users->
                        dispatch(UsersAction.UsersList(users))
                    }
                }
                action
            }
            UsersAction.DeleteUsers->{
                viewModelScope.launch {
                    userService.deleteAllUsers()
                    dispatch(UsersAction.UsersList(emptyList()))
                }
                action
            }
            UsersAction.AddUser->{
                viewModelScope.launch {
                    val name=state.usersScreenState.name
                    val address=state.usersScreenState.address
                    val age=state.usersScreenState.age
                    val user= User(
                        name = name,
                        address = address,
                        age = age.toInt()
                    )
                    userService.addUser(user)
                }
                action
            }
            is UsersAction.SelectUser->{
                viewModelScope.launch {
                    val user=userService.getUser(action.id)
                    dispatch(UsersAction.SelectedUser(user))
                }
                action
            }
            is UsersAction.DeleteUser->{
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
        dispatch?.invoke(UsersAction.GetUsers)
    }

    fun onEvent(usersAction: UsersAction){
        dispatch?.invoke(usersAction)
    }

    override fun onCleared() {
        super.onCleared()
        dispatch=null
    }
}