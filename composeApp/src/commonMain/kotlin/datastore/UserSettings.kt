package datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Serializer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.File
import java.io.InputStream
import java.io.OutputStream

@Serializable
data class UserSettings(
    val name:String="",
    val age:Int=0,
    val address:String=""
)

const val userSettings_filename="userSettings.json"

object UserSerializer:Serializer<UserSettings>{
    override val defaultValue: UserSettings
        get() = UserSettings()

    override suspend fun readFrom(input: InputStream): UserSettings {
        return try {
            Json.decodeFromString<UserSettings>(
                string = input.readBytes().decodeToString())
        }catch (e:SerializationException){
            e.printStackTrace()
            defaultValue
        }
    }

    override suspend fun writeTo(t: UserSettings , output: OutputStream) {
        withContext(Dispatchers.IO) {
            output.write(
                Json.encodeToString(
                    serializer = UserSettings.serializer(),
                    value = t).encodeToByteArray()
            )
        }
    }
}

fun createDataStore(
    path:()->File
):DataStore<UserSettings>{
    return DataStoreFactory.create(
        serializer = UserSerializer,
        produceFile = path,
        corruptionHandler = null
    )
}
