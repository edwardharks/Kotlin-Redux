package com.edwardharker.redux

import java.util.*

interface Action

class Store<S>(private val reducer: (S, Action) -> S, initialState: S) {

    var state: S = initialState
        private set
    private val listeners = HashSet<(S) -> Unit>()
    private var isDispatching = false
    private val lock = Object()


    fun dispatch(action: Action) {
        synchronized(lock) {
            if (isDispatching) {
                throw IllegalStateException("Reducers may not dispatch actions.")
            }

            try {
                isDispatching = true
                state = reducer(state, action)
            } finally {
                isDispatching = false
            }
        }

        for (listener in listeners) {
            listener(state)
        }
    }


    fun subscribe(listener: (S) -> Unit): () -> Unit {
        listeners.add(listener)
        listener(state)

        val unsubscriber = fun() {
            listeners.remove(listener)
        }

        return unsubscriber
    }

    companion object {
        fun <S> create(reducer: (S, Action) -> S, initialState: S): Store<S> = Store(reducer, initialState)
    }
}
