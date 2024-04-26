package datastore

import androidx.datastore.core.DataStore
import kotlinx.coroutines.flow.Flow

class UserSettingsServiceImpl(private val dataStore: DataStore<UserSettings>) : UserSettingsService {
    override suspend fun addUser(name: String , age: Int , address: String) {
        dataStore.updateData {
            it.copy(name = name , age = age , address = address)
        }
    }

    override fun getUserSettings(): Flow<UserSettings> {
        return dataStore.data
    }
}