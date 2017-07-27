package com.edwardharker.redux

import java.util.*

interface Action

interface State

class Store<out S : State>(private val reducer: (S, Action) -> S, private val initialState: S) {

    private val listeners = HashSet<(S) -> Unit>()

    private var state: S = initialState

    fun dispatch(action: Action) {
        state = reducer(initialState, action)

        for (listener in listeners) {
            listener(state)
        }
    }

    fun subscribe(listener: (S) -> Unit): () -> Unit {
        listeners.add(listener)

        val unsubscriber = fun() {
            listeners.remove(listener)
        }

        return unsubscriber
    }

    fun getState(): S = state
}
