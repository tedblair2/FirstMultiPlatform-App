package service

import model.User

interface UserService {
    suspend fun addUser(user: User)
    suspend fun getUsers():List<User>
    suspend fun deleteAllUsers()

    suspend fun getUser(id:Int):User?
}