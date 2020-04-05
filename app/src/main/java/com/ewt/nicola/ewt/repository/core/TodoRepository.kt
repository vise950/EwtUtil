package com.ewt.nicola.ewt.repository.core

import android.content.Context
import com.ewt.nicola.common.extension.isConnectionAvailable
import com.ewt.nicola.ewt.model.Todo
import com.ewt.nicola.ewt.repository.local.TodoLocalRepository
import com.ewt.nicola.ewt.repository.remote.TodoRemoteRepository
import com.ewt.nicola.realm.util.RealmLiveData

class TodoRepository(private val context: Context) {

    private val remoteRepo = TodoRemoteRepository()
    private val localRepo = TodoLocalRepository()

    suspend fun fetchTodo(id: String) {
        if (context.isConnectionAvailable())
            remoteRepo.fetchTodo(id)
    }

    suspend fun fetchTodo1(id: String): Todo? {
        if (context.isConnectionAvailable())
            return remoteRepo.fetchTodo1(id)
        return null
    }

    suspend fun fetchTodo2(id: String) = remoteRepo.fetchTodo2(id)

    suspend fun fetchTodo3(id: String) = remoteRepo.fetchTodo3(id)


    fun getTodo(id: Int): RealmLiveData<Todo> = localRepo.getTodo(id)

    suspend fun getTodo1(id: String) = localRepo.getTodo1(id)

    fun getTodos(): RealmLiveData<Todo> = localRepo.getTodos()

}