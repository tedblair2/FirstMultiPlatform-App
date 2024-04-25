package sqlDelight.events

import store.Action

sealed interface AddUserEvents:Action {
    data class UpdateName(val name:String):AddUserEvents
    data class UpdateAge(val age:String):AddUserEvents
    data class UpdateAddress(val address:String):AddUserEvents
    data object SaveUserToDb:AddUserEvents
    data object ResetValues:AddUserEvents
}