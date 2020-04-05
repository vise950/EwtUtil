package com.ewt.nicola.ewt.repository.local

import com.ewt.nicola.ewt.application.Init
import com.ewt.nicola.ewt.model.Todo
import com.ewt.nicola.ewt.util.todoDao
import com.ewt.nicola.realm.util.RealmLiveData

class TodoLocalRepository {

    private val todoDao = Init.getRealmInstance().todoDao()


    fun getTodo(id: Int): RealmLiveData<Todo> = todoDao.getTodo(id)

    suspend fun getTodo1(id: String) = todoDao.getTodo1()

    fun getTodos(): RealmLiveData<Todo> = todoDao.getTodos()
}