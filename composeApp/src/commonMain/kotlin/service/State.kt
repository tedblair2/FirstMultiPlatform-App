package service

import model.AppState

interface Action

typealias Reducer<S> = (old:S,action:Action)->S
typealias Dispatch = (action:Action)->Unit
typealias Subscriber = (dispatch:Dispatch)->Unit
typealias MiddleWare = (state:AppState, action:Action,dispatch:Dispatch, next:Next)->Action
typealias Next = (state: AppState , action:Action , dispatch:Dispatch)->Action