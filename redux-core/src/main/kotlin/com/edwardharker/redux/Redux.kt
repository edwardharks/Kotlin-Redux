package com.edwardharker.redux

interface Action

interface State

class Store<out S : State> {

    fun dispatch(action: Action) = Unit

    fun subscribe(listener: (S) -> Unit) = Unit

    fun getState(): S = TODO()
}
