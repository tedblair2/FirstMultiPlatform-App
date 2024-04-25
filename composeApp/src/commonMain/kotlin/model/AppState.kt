package model

import sqlDelight.model.AddUserScreenState
import sqlDelight.model.SqlUsersScreenState

data class AppState(
    val counterState: CounterState=CounterState(),
    val countryScreenState: CountryScreenState=CountryScreenState(),
    val usersScreenState: UsersScreenState = UsersScreenState(),
    val sqlUsersScreenState: SqlUsersScreenState = SqlUsersScreenState(),
    val addUserScreenState:AddUserScreenState=AddUserScreenState()
)
