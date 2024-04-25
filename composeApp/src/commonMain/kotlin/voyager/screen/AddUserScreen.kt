package voyager.screen

import AddSqlUserScreen
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.koin.getScreenModel
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import voyager.screenmodel.AddUserScreenModel

class AddUserScreen:Screen{
    @Composable
    override fun Content() {
        val screenModel=getScreenModel<AddUserScreenModel>()
        val state by screenModel.state.collectAsState()
        val navigator= LocalNavigator.currentOrThrow

        AddSqlUserScreen(
            addUserScreenState = state,
            onEvent = screenModel::onEvent,
            onNavigateUp = {
                navigator.pop()
            }
        )
    }
}
