package datastore

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserSettingsScreenModel(
    private val userSettingsService: UserSettingsService
):StateScreenModel<UserSettingsScreenState>(UserSettingsScreenState()) {

    init {
        screenModelScope.launch {
            userSettingsService.getUserSettings().collect{userSettings->
                mutableState.update {
                    it.copy(userSettings = userSettings)
                }
            }
        }
    }
    fun onEvent(action: UserSettingsScreenActions){
        when(action){
            is UserSettingsScreenActions.UpdateAddress->{
                mutableState.update {
                    it.copy(address = action.address)
                }
            }
            is UserSettingsScreenActions.UpdateName->{
                mutableState.update {
                    it.copy(name = action.name)
                }
            }
            is UserSettingsScreenActions.UpdateAge->{
                mutableState.update {
                    it.copy(age = action.age)
                }
            }
            UserSettingsScreenActions.AddUserSettings->{
                screenModelScope.launch {
                    val currentState=state.value
                    userSettingsService.addUser(
                        name = currentState.name,
                        address = currentState.address,
                        age = currentState.age.toInt()
                    )
                    mutableState.update {
                        it.copy(name = "", address = "", age = "")
                    }
                }
            }
        }
    }
}