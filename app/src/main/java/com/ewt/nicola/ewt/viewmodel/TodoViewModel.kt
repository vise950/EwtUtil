package com.ewt.nicola.ewt.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.ewt.nicola.ewt.model.Todo
import com.ewt.nicola.ewt.repository.core.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class TodoViewModel(context: Context) : ViewModel() {

    private val job = SupervisorJob()
    private val coroutineContext = Dispatchers.IO + job

    private val repo: TodoRepository = TodoRepository(context)

    fun fetchTodo(id: String) {
        liveData(coroutineContext) {
            emit(repo.fetchTodo(id))
        }
    }

    val todo1: LiveData<Todo?> = liveData(coroutineContext) {
        val retrivedTodo = repo.fetchTodo1("1")
        emit(retrivedTodo)
    }

    val todo2 = liveData(coroutineContext) {
        val retrivedTodo = repo.fetchTodo2("1")
        emit(retrivedTodo)
    }

    val todo3 = liveData(coroutineContext) {
        val retrivedTodo = repo.fetchTodo3("1")
        emit(retrivedTodo)
    }

    val todo4 = liveData(coroutineContext) {
        repo.fetchTodo3("1")
        emit(repo.getTodo1("1"))
    }

    val todo5 = liveData(coroutineContext) {
        emit(repo.getTodo(1))
//        emitSource(repo.fetchTodo3("1"))
    }

    fun getTodo(id: Int) = repo.getTodo(id)
}