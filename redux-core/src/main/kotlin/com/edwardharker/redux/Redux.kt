package com.edwardharker.redux

import java.util.*

interface Action

interface Store<out S> {

    fun dispatch(action: Action)

    fun subscribe(listener: (S) -> Unit): () -> Unit

    fun getState(): S
}

class DefaultStore<out S>(private val reducer: (S, Action) -> S, initialState: S) : Store<S> {

    private var state: S = initialState
    private val listeners = HashSet<(S) -> Unit>()
    private var isDispatching = false

    override fun dispatch(action: Action) {
        if (isDispatching) {
            throw IllegalStateException("Reducers may not dispatch actions.")
        }

        try {
            isDispatching = true
            state = reducer(state, action)
        } finally {
            isDispatching = false
        }

        for (listener in listeners) {
            listener(state)
        }
    }

    override fun subscribe(listener: (S) -> Unit): () -> Unit {
        listeners.add(listener)
        listener(state)

        val unsubscriber = fun() {
            listeners.remove(listener)
        }

        return unsubscriber
    }

    override fun getState(): S = state

    companion object {
        fun <S> create(reducer: (S, Action) -> S, initialState: S): Store<S> = DefaultStore(reducer, initialState)
    }
}

class ThreadSafeStore<out S>(reducer: (S, Action) -> S, initialState: S) : Store<S> {

    private val defaultStore = DefaultStore(reducer, initialState)

    private val lock = Object()

    override fun dispatch(action: Action) {
        synchronized(lock) {
            defaultStore.dispatch(action)
        }
    }

    override fun subscribe(listener: (S) -> Unit): () -> Unit {
        synchronized(lock) {
            return defaultStore.subscribe(listener)
        }
    }

    override fun getState(): S {
        synchronized(lock) {
            return defaultStore.getState()
        }
    }

    companion object {
        fun <S> create(reducer: (S, Action) -> S, initialState: S): Store<S> = ThreadSafeStore(reducer, initialState)
    }
}
