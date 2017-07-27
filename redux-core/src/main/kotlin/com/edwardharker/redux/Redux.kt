package com.edwardharker.redux

import java.util.*

interface Action

interface State

interface Store<out S : State> {

    fun dispatch(action: Action)

    fun subscribe(listener: (S) -> Unit): () -> Unit

    fun getState(): S

    companion object {
        fun <S : State> create(reducer: (S, Action) -> S, initialState: S): Store<S> = DefaultStore(reducer, initialState)
    }
}

class DefaultStore<out S : State>(private val reducer: (S, Action) -> S, private val initialState: S) : Store<S> {

    private val listeners = HashSet<(S) -> Unit>()

    private var state: S = initialState

    override fun dispatch(action: Action) {
        state = reducer(initialState, action)

        for (listener in listeners) {
            listener(state)
        }
    }

    override fun subscribe(listener: (S) -> Unit): () -> Unit {
        listeners.add(listener)

        val unsubscriber = fun() {
            listeners.remove(listener)
        }

        return unsubscriber
    }

    override fun getState(): S = state
}
