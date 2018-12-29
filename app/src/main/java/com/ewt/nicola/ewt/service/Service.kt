package com.ewt.nicola.ewt.service

import com.ewt.nicola.ewt.model.User
import io.realm.RealmList
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface Service {
    @GET("todos/1")
    fun getTodo(): Deferred<Response<User>>

    @GET("todos")
    fun getTodos(): Deferred<Response<RealmList<User>>>
}