
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import di.commonModule
import di.desktopModule
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject

fun main() = application {
    startKoin {
        modules(commonModule,desktopModule)
    }

    val controller:AppController by inject(AppController::class.java)
    val countryController:CountryController by inject(CountryController::class.java)
    val counterState by controller.counterState.collectAsState()
    val countryScreenState by countryController.countryState.collectAsState()

    Window(onCloseRequest = {
        controller.onCleared()
        countryController.onCleared()
        exitApplication()
    }, title = "FirstMultiplatform") {
        App(
            countryScreenState = countryScreenState,
            onEvent = countryController::onEvent,
            gridCount = 3
        )
    }
}