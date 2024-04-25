package voyager.screen

import SqlUsersScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import voyager.screenmodel.SqlUsersScreenModel

class HomeScreen:Screen {
    @Composable
    override fun Content() {
        val screenModel=getScreenModel<SqlUsersScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator= LocalNavigator.currentOrThrow

        SqlUsersScreen(
            sqlUsersScreenState = state,
            onEvent = screenModel::onEvent,
            onNavigateToAdd = {
                navigator.push(AddUserScreen())
            }
        )
    }
}