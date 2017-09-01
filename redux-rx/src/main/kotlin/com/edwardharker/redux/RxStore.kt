package com.edwardharker.redux

import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.disposables.Disposable

fun <S> Store<S>.dispatch(actions: Flowable<out Action>): Disposable = actions.subscribe { dispatch(it) }

fun <S> Store<S>.asObservable(): Flowable<out S> = Flowable.create({ emitter ->
    val unsubscriber = subscribe { emitter.onNext(it) }
    emitter.setCancellable(unsubscriber)
}, BackpressureStrategy.BUFFER)
