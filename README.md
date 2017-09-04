# Kotlin-Redux
An implementation of Redux written entirely in Kotlin with RxJava 2 extensions

See http://redux.js.org/ for an explanation of what Redux is

### Example

```kotlin
// Actions describe a change in the state
object Increment : Action
object Decrement : Action

// The reducer is a function which takes the old state and an action. 
// It mutates the state depending on the action it receives
fun counter(state: Int, action: Action) =
        when (action) {
            is Increment -> state + 1
            is Decrement -> state - 1
            else -> state
        }

fun main(args: Array<String>) {
    // Create a store
    val store = Store.create(::counter, 0)
    
    // Listen to changes
    store.subscribe { state -> print(state) }

    // Dispatch actions
    store.dispatch(Increment)
    // 1
    store.dispatch(Increment)
    // 2
    store.dispatch(Decrement)
    // 1
}
```
