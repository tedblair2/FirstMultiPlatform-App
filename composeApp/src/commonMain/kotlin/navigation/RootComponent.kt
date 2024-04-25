package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.pushNew
import kotlinx.serialization.Serializable

@OptIn(ExperimentalDecomposeApi::class)
class RootComponent(
    componentContext: ComponentContext
):ComponentContext by componentContext {

    private val navigation=StackNavigation<Configuration>()

    val childStack= childStack(
        source = navigation,
        serializer = Configuration.serializer(),
        initialConfiguration = Configuration.ScreenA,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        configuration: Configuration,
        context: ComponentContext
    ):Child{
        return when(configuration){
            is Configuration.ScreenA->{
                Child.ScreenA(ScreenAComponent(
                    componentContext = context,
                    onNavigateToScreenB = {
                        navigation.pushNew(Configuration.ScreenB(it))
                    })
                )
            }
            is Configuration.ScreenB->{
                Child.ScreenB(ScreenBComponent(
                    text = configuration.text,
                    componentContext = context,
                    onNavigateUp = {
                        navigation.pop()
                    }))
            }
        }
    }
    sealed class Child{
        data class ScreenA(val screenAComponent: ScreenAComponent):Child()
        data class ScreenB(val screenBComponent: ScreenBComponent):Child()
    }
    @Serializable
    sealed class Configuration{
        @Serializable
        data object ScreenA:Configuration()
        @Serializable
        data class ScreenB(val text:String):Configuration()
    }
}