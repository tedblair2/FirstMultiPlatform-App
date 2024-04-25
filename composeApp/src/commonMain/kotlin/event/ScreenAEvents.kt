package event

sealed interface ScreenAEvents {
    data object ClickButton:ScreenAEvents
    data class EnterText(val text:String):ScreenAEvents
}