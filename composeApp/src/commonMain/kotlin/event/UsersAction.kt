package event

import model.User
import service.Action

sealed interface UsersAction:Action {
    data object GetUsers:UsersAction
    data class UsersList(val users:List<User>):UsersAction
    data class UpdateName(val name:String):UsersAction
    data class UpdateAddress(val address:String):UsersAction
    data class UpdateAge(val age:String):UsersAction
    data object AddUser:UsersAction
    data object DeleteUsers:UsersAction
    data object ResetValues:UsersAction
    data class SelectUser(val id:Int):UsersAction
    data class SelectedUser(val user: User?):UsersAction
    data object DismissDialog:UsersAction
}