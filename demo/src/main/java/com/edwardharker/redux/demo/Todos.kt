package com.edwardharker.redux.demo

import com.edwardharker.redux.Action

fun reduce(todos: List<Todo>, action: Action): List<Todo> =
        when (action) {
            is AddTodoAction -> todos.toMutableList().apply { add(action.todo) }
            is RemoveTodoAction -> todos.toMutableList().apply { removeIf { action.todo.id == it.id } }
            else -> todos
        }

data class Todo(val id: Long, val text: String)

data class AddTodoAction(val todo: Todo) : Action
data class RemoveTodoAction(val todo: Todo) : Action