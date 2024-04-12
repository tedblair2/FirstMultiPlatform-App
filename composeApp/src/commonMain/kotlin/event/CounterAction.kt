package event

import service.Action

sealed interface CounterAction: Action {
    data object Init:CounterAction
    data object Increment:CounterAction
    data object Decrement:CounterAction
    data object LoadData:CounterAction
    data class DataLoaded(val value:Int):CounterAction
}