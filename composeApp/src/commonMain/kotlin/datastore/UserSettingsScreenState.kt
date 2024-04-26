package datastore

data class UserSettingsScreenState(
    val name:String="",
    val address:String="",
    val age:String="",
    val userSettings: UserSettings= UserSettings()
)

sealed interface UserSettingsScreenActions{
    data class UpdateName(val name: String):UserSettingsScreenActions
    data class UpdateAge(val age: String):UserSettingsScreenActions
    data class UpdateAddress(val address: String):UserSettingsScreenActions
    data object AddUserSettings:UserSettingsScreenActions
}
