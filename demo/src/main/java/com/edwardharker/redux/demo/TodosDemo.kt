@file:JvmName("ReduxDemo")

package com.edwardharker.redux.demo

import com.edwardharker.redux.Store

fun main(args: Array<String>) {
    val store = Store.create(::reduce, emptyList())

    val unsubscribe = store.subscribe { todos -> println(todos) }

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

    unsubscribe()

    println("dispatching: $addTodo2")
    store.dispatch(addTodo2)

    val unsubscribe2 = store.subscribe { println(it) }
    unsubscribe2()
}
