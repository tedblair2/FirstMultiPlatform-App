package model

data class UsersScreenState(
    val users:List<User> = emptyList(),
    val name:String="",
    val address:String="",
    val age:String="",
    val selectedUser: User?=null
)
