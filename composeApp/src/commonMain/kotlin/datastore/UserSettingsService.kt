package datastore

import kotlinx.coroutines.flow.Flow

interface UserSettingsService {
    suspend fun addUser(name:String,age:Int,address:String)
    fun getUserSettings():Flow<UserSettings>
}