package navigation

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.update
import event.ScreenAEvents

class ScreenAComponent(
    componentContext: ComponentContext,
    private val onNavigateToScreenB:(String)->Unit
):ComponentContext by componentContext {

    private val _text= MutableValue("")
    val text:Value<String> = _text

    fun onEvent(event:ScreenAEvents){
        when(event){
            ScreenAEvents.ClickButton->{
                onNavigateToScreenB(text.value)
            }
            is ScreenAEvents.EnterText->{
                _text.update {
                    event.text
                }
            }
        }
    }
}