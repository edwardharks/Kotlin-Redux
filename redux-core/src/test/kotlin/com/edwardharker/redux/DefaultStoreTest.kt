package com.edwardharker.redux

import org.hamcrest.CoreMatchers.equalTo
import org.junit.Assert.assertThat
import org.junit.Test

§// TODO more tests
class DefaultStoreTest {

    @Test
    fun dispatchActionProvidesActionToReducer() {
        var reducedAction: Action? = null

        val reducer = fun(state: FakeState, action: Action): FakeState {
            reducedAction = action
            return state
        }

        val store = DefaultStore(reducer, FakeState())

        store.dispatch(FakeAction)
        assertThat(reducedAction, equalTo(FakeAction as Action))
    }

    @Test
    fun dispatchActionProvidesInitialStateToReducer() {
        var oldState: State? = null

        val reducer = fun(state: FakeState, action: Action): FakeState {8
            oldState = state
            return state
        }

        val expected = FakeState()
        val store = DefaultStore(reducer, expected)
        store.dispatch(FakeAction)
        assertThat(oldState, equalTo(expected as State))
    }

    @Test
    fun dispatchActionNotifiesListenersWithNewState() {
        val reducer = fun(state: FakeState, action: Action) = state

        val expected = object : FakeState() {}
        val store = DefaultStore(reducer, expected)

        var observedState: FakeState? = null
        store.subscribe { state -> observedState = state }
        store.dispatch(FakeAction)

        assertThat(observedState, equalTo(expected as FakeState))
    }

    @Test
    fun getStateReturnsCurrentState() {
        val expected = object : FakeState() {}

        val reducer = fun(state: FakeState, action: Action) = expected
        val store = DefaultStore(reducer, FakeState())

        store.dispatch(FakeAction)
        assertThat(store.getState(), equalTo(expected as FakeState))
    }

    private open class FakeState : State
    private object FakeAction : Action
}
