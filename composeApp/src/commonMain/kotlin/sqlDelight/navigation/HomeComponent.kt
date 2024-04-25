package sqlDelight.navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable
import store.Store

@OptIn(ExperimentalDecomposeApi::class)
class HomeComponent(
    componentContext: ComponentContext,
    private val store: Store
):ComponentContext by componentContext {

    private val navigation= StackNavigation<Configuration>()

    val childStack=childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.Users,
        handleBackButton = true,
        childFactory = ::createChild
    )
    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ):ChildScreens{
        return when(configuration){
            Configuration.Users->{
                ChildScreens.Users(
                    UsersComponent(
                        componentContext = context,
                        onNavigateToUserDetails = {

                        },
                        onNavigateToAdduser = {
                            navigation.pushNew(Configuration.AddUser)
                        },
                        store = store
                    )
                )
            }
            Configuration.AddUser->{
                ChildScreens.AddUser(
                    AddUserComponent(
                        componentContext = context,
                        onNavigateUp = {
                            navigation.pop()
                        }
                    )
                )
            }
        }
    }

    sealed class ChildScreens{
        data class Users(val usersComponent: UsersComponent):ChildScreens()
        data class AddUser(val addUserComponent: AddUserComponent):ChildScreens()
    }


    @Serializable
    sealed class Configuration{
        @Serializable
        data object Users: Configuration()
        @Serializable
        data object AddUser: Configuration()
    }
}