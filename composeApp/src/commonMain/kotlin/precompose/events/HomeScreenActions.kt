package precompose.events

import model.User
import store.Action

sealed interface HomeScreenActions:Action {
    data object GetUsers: HomeScreenActions
    data class UsersList(val users:List<User>): HomeScreenActions
    data class DeleteUser(val id: Int): HomeScreenActions
}