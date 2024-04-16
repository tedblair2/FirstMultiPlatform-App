package service

import com.github.tedblair2.db.AppDatabase
import com.github.tedblair2.db.Users
import model.User

class UserServiceImpl(
    db:AppDatabase
) : UserService {

    private val dbQuery=db.appDatabaseQueries
    override suspend fun addUser(user: User) {
        dbQuery.insertUser(user.name,user.age.toLong(),user.address)
    }

    override suspend fun getUsers(): List<User> {
        return dbQuery.getAllUsers().executeAsList().map { it.toUser() }
    }

    override suspend fun deleteAllUsers() {
        dbQuery.transaction {
            dbQuery.deleteUsers()
        }
    }

    override suspend fun getUser(id: Int): User? {
        return dbQuery.getUser(id.toLong()).executeAsOneOrNull()?.toUser()
    }

    private fun Users.toUser():User{
        return User(
            id = id.toInt(),
            name = name,
            address = address,
            age = age.toInt()
        )
    }
}