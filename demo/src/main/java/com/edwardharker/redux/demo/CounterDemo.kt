package com.edwardharker.redux.demo

import com.edwardharker.redux.Action
import com.edwardharker.redux.Store

object Increment : Action
object Decrement : Action

fun counter(state: Int, action: Action) =
        when (action) {
            is Increment -> state + 1
            is Decrement -> state - 1
            else -> state
        }

fun main(args: Array<String>) {

    val store = Store.create(::counter, 0)

    store.subscribe { state -> print(state) }

    store.dispatch(Increment)
    // 1
    store.dispatch(Increment)
    // 2
    store.dispatch(Decrement)
    // 1
}