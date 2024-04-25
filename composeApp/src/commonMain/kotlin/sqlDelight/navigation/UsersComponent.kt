package sqlDelight.navigation

import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import model.AppState
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import service.AppCoroutineContext
import service.UserService
import sqlDelight.events.UsersScreenActions
import sqlDelight.model.SqlUsersScreenState
import store.Dispatch
import store.MiddleWare
import store.Reducer
import store.Store

class UsersComponent(
    componentContext: ComponentContext,
    private val onNavigateToUserDetails:(id:Int)->Unit,
    private val onNavigateToAdduser:()->Unit,
    store: Store,
):ComponentContext by componentContext,KoinComponent {

    private val appCoroutineContext:AppCoroutineContext by inject<AppCoroutineContext>()
    private val userService:UserService by inject<UserService>()
    private val componentScope= CoroutineScope(appCoroutineContext.io+ SupervisorJob())
    private var dispatch:Dispatch?=null

    val state=store.getCurrentState { dispatch=it }
        .map { it.sqlUsersScreenState }
        .stateIn(componentScope, SharingStarted.WhileSubscribed(5000),SqlUsersScreenState())

    private val reducer:Reducer<SqlUsersScreenState> = {old, action ->
        when(action){
            is UsersScreenActions.UsersList->old.copy(users = action.users)
            else -> old
        }
    }
    private val appReducer:Reducer<AppState> ={old, action ->
        old.copy(sqlUsersScreenState = reducer(old.sqlUsersScreenState,action))
    }
    private val middleWare:MiddleWare = {state, action, dispatch, next ->
        when(action){
            UsersScreenActions.GetUsers->{
                componentScope.launch {
                    userService.getUsers().collect{users->
                        dispatch(UsersScreenActions.UsersList(users))
                    }
                }
                action
            }
            is UsersScreenActions.DeleteUser->{
                componentScope.launch {
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
        dispatch?.invoke(UsersScreenActions.GetUsers)
    }

    fun onEvent(action: UsersScreenActions){
        dispatch?.invoke(action)
    }
}