@file:JvmName("RxReduxDemo")

package com.edwardharker.redux.demo

import com.edwardharker.redux.RxStore


fun main(args: Array<String>) {
    val store = RxStore.create(::reduce, emptyList())

    val disposable = store.asObservable().subscribe { println(it) }

    val todo1 = Todo(1, "Make a redux demo")
    val addTodo1 = AddTodoAction(todo1)
    val removeTodo1 = RemoveTodoAction(todo1)

    val todo2 = Todo(2, "Add another todo")
    val addTodo2 = AddTodoAction(todo2)
    val removeTodo2 = RemoveTodoAction(todo2)

    println("dispatching: $addTodo1")
    store.dispatch(addTodo1)

    println("dispatching: $removeTodo1")
    store.dispatch(removeTodo1)

    println("dispatching: $addTodo2")
    store.dispatch(addTodo2)

    println("dispatching: $addTodo1")
    store.dispatch(addTodo1)

    println("dispatching: $removeTodo1")
    store.dispatch(removeTodo1)

    println("dispatching: $removeTodo2")
    store.dispatch(removeTodo2)

    disposable.dispose()

    println("dispatching: $addTodo2")
    store.dispatch(addTodo2)

    val disposable2 = store.asObservable().subscribe { println(it) }
    disposable2.dispose()
}