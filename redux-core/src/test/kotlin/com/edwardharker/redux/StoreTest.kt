package com.edwardharker.redux

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

private open class FakeState
private object FakeAction : Action

class StoreTest {

    @Test
    fun `dispatch action provides action to reducer`() {
        var reducedAction: Action? = null

        val reducer = fun(state: FakeState, action: Action): FakeState {
            reducedAction = action
            return state
        }

        val store = Store.create(reducer, FakeState())

        store.dispatch(FakeAction)
        assertThat(reducedAction, equalTo(FakeAction as Action))
    }

    @Test
    fun `dispatch action provides initial state to reducer`() {
        var oldState: FakeState? = null

        val reducer = fun(state: FakeState, _: Action): FakeState {
            oldState = state
            return state
        }

        val expected = FakeState()
        val store = Store.create(reducer, expected)
        store.dispatch(FakeAction)
        assertThat(oldState, equalTo(expected))
    }

    @Test
    fun `dispatch action notifies listeners with new state`() {
        val reducer = fun(state: FakeState, _: Action) = state

        val expected = object : FakeState() {}
        val store = Store.create(reducer, expected)

        var observedState: FakeState? = null
        store.subscribe { state -> observedState = state }
        store.dispatch(FakeAction)

        assertThat(observedState, equalTo(expected as FakeState))
    }

    @Test
    fun `get state returns current state`() {
        val expected = object : FakeState() {}

        val reducer = fun(_: FakeState, _: Action) = expected
        val store = Store.create(reducer, FakeState())

        store.dispatch(FakeAction)
        assertThat(store.state, equalTo(expected as FakeState))
    }

    @Test
    fun `get state returns initial state before any actions have been dispatched`() {
        val expected = object : FakeState() {}

        val reducer = fun(_: FakeState, _: Action) = FakeState()
        val store = Store.create(reducer, expected)

        assertThat(store.state, equalTo(expected as FakeState))
    }
}
