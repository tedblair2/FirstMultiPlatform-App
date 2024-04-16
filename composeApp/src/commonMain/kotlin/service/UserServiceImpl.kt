package service

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.github.tedblair2.db.AppDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import model.User

class UserServiceImpl(
    db:AppDatabase,
    private val appCoroutineContext: AppCoroutineContext
) : UserService {

    private val dbQuery=db.appDatabaseQueries
    override suspend fun addUser(user: User) {
        withContext(appCoroutineContext.io){
            dbQuery.insertUser(user.name,user.age.toLong(),user.address)
        }
    }

    override suspend fun getUsers(): Flow<List<User>> {
        return dbQuery.getAllUsers(::mapToUser)
            .asFlow()
            .mapToList(appCoroutineContext.io)
    }

    override suspend fun deleteAllUsers() {
        withContext(appCoroutineContext.io){
            dbQuery.transaction {
                dbQuery.deleteUsers()
            }
        }
    }

    override suspend fun getUser(id: Int): User? {
        return withContext(appCoroutineContext.io){
            dbQuery.getUser(id.toLong(),::mapToUser).executeAsOneOrNull()
        }
    }

    override suspend fun deleteUser(id: Int) {
        withContext(appCoroutineContext.io){
            dbQuery.deleteUser(id.toLong())
        }
    }

    private fun mapToUser(
        id: Long,
        name:String,
        age:Long,
        address:String
    ):User{
        return User(id.toInt(),name,age.toInt(),address)
    }
}