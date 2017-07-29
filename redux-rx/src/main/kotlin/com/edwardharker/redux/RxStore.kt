package com.edwardharker.redux

import io.reactivex.Flowable
import io.reactivex.BackpressureStrategy


class RxStore<out S : State>(reducer: (S, Action) -> S, initialState: S) : Store<S> {

    private val store = ThreadSafeStore(reducer, initialState)

    override fun dispatch(action: Action) {
        store.dispatch(action)
    }

    override fun subscribe(listener: (S) -> Unit): () -> Unit {
        return store.subscribe(listener)
    }

    fun asObservable(): Flowable<out S> = Flowable.create({ emitter ->
        val unsubscriber = subscribe { emitter.onNext(it) }
        emitter.setCancellable(unsubscriber)
    }, BackpressureStrategy.BUFFER)

    override fun getState(): S {
        return store.getState()
    }

    companion object {
        fun <S : State> create(reducer: (S, Action) -> S, initialState: S): RxStore<S> = RxStore(reducer, initialState)
    }
}
