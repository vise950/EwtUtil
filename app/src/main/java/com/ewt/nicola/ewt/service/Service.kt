package com.ewt.nicola.ewt.service

import com.ewt.nicola.ewt.model.Todo
import io.realm.RealmList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Service {

    @GET("todos/{id}")
    suspend fun fetchTodo(@Path("id") id: String): Response<Todo>

    @GET("todos")
    suspend fun fetchTodos(): Response<RealmList<Todo>>


    @GET("todos/{id}")
    suspend fun fetchTodo1(@Path("id") id: String): Todo

    @GET("todos")
    suspend fun fetchTodos1(): RealmList<Todo>
}