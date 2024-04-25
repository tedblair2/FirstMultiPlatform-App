package sqlDelight.model

import model.User

data class SqlUsersScreenState(
    val users:List<User> = emptyList()
)
