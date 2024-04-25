package store

import kotlinx.coroutines.flow.Flow
import model.AppState

interface Store {
    fun applyMiddleWare(middleWare: MiddleWare): Store
    fun applyReducer(reducer: Reducer<AppState>): Store
    fun getCurrentState(subscriber: Subscriber): Flow<AppState>
}