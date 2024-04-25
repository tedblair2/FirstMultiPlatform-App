
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.commonModule
import di.desktopModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject
import theme.AppTheme

fun main() {
    application {
        startKoin {
            modules(commonModule, desktopModule)
        }
        val controller: AppController by inject(AppController::class.java)
        val countryController: CountryController by inject(CountryController::class.java)
        val usersController: UsersController by inject(UsersController::class.java)
        val counterState by controller.counterState.collectAsState()
        val countryScreenState by countryController.countryState.collectAsState()
        val usersScreenState by usersController.usersState.collectAsState()

        Window(onCloseRequest = {
            controller.onCleared()
            countryController.onCleared()
            usersController.onCleared()
            exitApplication()
        } , title = "FirstMultiplatform") {
            App()
        }
    }
}

@Composable
fun ScrollBar(state:LazyGridState) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(state),
        modifier = Modifier.fillMaxHeight()
            .padding(end = 4.dp),
        style = ScrollbarStyle(
            thickness = 12.dp,
            shape = RoundedCornerShape(18.dp),
            minimalHeight = 100.dp,
            hoverColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.8f),
            unhoverColor = AppTheme.colorScheme.onBackground.copy(alpha = 0.5f),
            hoverDurationMillis = 1000
        )
    )
}