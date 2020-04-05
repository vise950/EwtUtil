package com.ewt.nicola.ewt.repository.remote

import com.ewt.nicola.common.extension.log
import com.ewt.nicola.ewt.application.Init
import com.ewt.nicola.ewt.service.Service
import com.ewt.nicola.network.make
import com.ewt.nicola.realm.extension.save

class TodoRemoteRepository {

    private val service = Init.getRetrofit().create(Service::class.java)
    private val realm = Init.getRealmInstance()

    suspend fun fetchTodo(id: String) {
        service.fetchTodo(id)
            .make()
            .then {
                it.save(realm)
            }
            .error {
                it.log("fetchTodo")
            }
    }

    suspend fun fetchTodos() {
        service.fetchTodos()
            .make()
            .then {
                it.save(realm)
            }
            .error {
                it.log("fetchTodos")
            }
    }


    suspend fun fetchTodo1(id: String) =
        service.fetchTodo1(id)

    suspend fun fetchTodo2(id: String) =
        service.fetchTodo1(id).save(realm)

    suspend fun fetchTodo3(id: String) =
        service.fetchTodo1(id)

    suspend fun fetchTodos1() =
        service.fetchTodos1().save(realm)
}