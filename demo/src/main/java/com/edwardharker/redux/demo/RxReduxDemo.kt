@file:JvmName("RxReduxDemo")

package com.edwardharker.redux.demo

import com.edwardharker.redux.Action
import com.edwardharker.redux.Store
import com.edwardharker.redux.asObservable
import com.edwardharker.redux.dispatch
import io.reactivex.BackpressureStrategy.*
import io.reactivex.subjects.PublishSubject


fun main(args: Array<String>) {
    val store = Store.create(::reduce, emptyList())

    val observerDisposable = store.asObservable().subscribe { println(it) }

    val dispatchSubject = PublishSubject.create<Action>()
    val dispatcherDisposable = store.dispatch(dispatchSubject.toFlowable(ERROR))

    val todo1 = Todo(1, "Make a redux demo")
    val addTodo1 = AddTodoAction(todo1)
    val removeTodo1 = RemoveTodoAction(todo1)

    val todo2 = Todo(2, "Add another todo")
    val addTodo2 = AddTodoAction(todo2)
    val removeTodo2 = RemoveTodoAction(todo2)

    println("dispatching: $addTodo1")
    dispatchSubject.onNext(addTodo1)

    println("dispatching: $removeTodo1")
    dispatchSubject.onNext(removeTodo1)

    println("dispatching: $addTodo2")
    dispatchSubject.onNext(addTodo2)

    println("dispatching: $addTodo1")
    dispatchSubject.onNext(addTodo1)

    println("dispatching: $removeTodo1")
    dispatchSubject.onNext(removeTodo1)

    println("dispatching: $removeTodo2")
    dispatchSubject.onNext(removeTodo2)

    observerDisposable.dispose()

    println("dispatching: $addTodo2")
    dispatchSubject.onNext(addTodo2)

    val disposable2 = store.asObservable().subscribe { println(it) }
    disposable2.dispose()

    dispatcherDisposable.dispose()
}