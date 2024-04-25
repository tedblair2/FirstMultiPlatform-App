package navigation

import com.arkivanov.decompose.ComponentContext

class ScreenBComponent(
    val text:String,
    componentContext: ComponentContext,
    private val onNavigateUp:()->Unit
):ComponentContext by componentContext {

    fun goBack(){
        onNavigateUp()
    }
}