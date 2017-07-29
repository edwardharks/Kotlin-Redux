package com.edwardharker.redux

import java.util.*

interface Action

interface State

interface Store<out S : State> {

    fun dispatch(action: Action)

    fun subscribe(listener: (S) -> Unit): () -> Unit

    fun getState(): S
}

class DefaultStore<out S : State>(private val reducer: (S, Action) -> S, private val initialState: S) : Store<S> {

    private val listeners = HashSet<(S) -> Unit>()

    private var state: S = initialState

    override fun dispatch(action: Action) {
        state = reducer(state, action)

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

    companion object {
        fun <S : State> create(reducer: (S, Action) -> S, initialState: S): Store<S> = DefaultStore(reducer, initialState)
    }
}

class ThreadSafeStore<out S : State>(reducer: (S, Action) -> S, initialState: S) : Store<S> {

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
        fun <S : State> create(reducer: (S, Action) -> S, initialState: S): Store<S> = ThreadSafeStore(reducer, initialState)
    }
}
