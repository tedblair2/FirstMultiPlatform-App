package service

import kotlinx.coroutines.CoroutineDispatcher

interface AppCoroutineContext {
    val main:CoroutineDispatcher
    val io:CoroutineDispatcher
    val default:CoroutineDispatcher
}