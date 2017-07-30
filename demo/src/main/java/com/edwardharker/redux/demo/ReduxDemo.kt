@file:JvmName("ReduxDemo")

package com.edwardharker.redux.demo

import com.edwardharker.redux.Action
import com.edwardharker.redux.DefaultStore


fun main(args: Array<String>) {
    val store = DefaultStore.create(::reduce, emptyList())

    val unsubscribe = store.subscribe { println(it) }

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

fun reduce(todos: List<Todo>, action: Action): List<Todo> =
        when (action) {
            is AddTodoAction -> todos.toMutableList().apply { add(action.todo) }
            is RemoveTodoAction -> todos.toMutableList().apply { removeIf { action.todo.id == it.id } }
            else -> todos
        }

data class Todo(val id: Long, val text: String)

data class AddTodoAction(val todo: Todo) : Action
data class RemoveTodoAction(val todo: Todo) : Action