package sqlDelight.events

import model.User
import store.Action

sealed interface UsersScreenActions:Action {
    data object GetUsers:UsersScreenActions
    data class UsersList(val users:List<User>):UsersScreenActions
    data class DeleteUser(val id: Int):UsersScreenActions
}