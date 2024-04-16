package service

import kotlinx.coroutines.flow.Flow
import model.User

interface UserService {
    suspend fun addUser(user: User)
    suspend fun getUsers(): Flow<List<User>>
    suspend fun deleteAllUsers()

    suspend fun getUser(id:Int):User?
    suspend fun deleteUser(id: Int)
}