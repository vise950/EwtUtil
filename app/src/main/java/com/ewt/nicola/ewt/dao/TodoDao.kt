package com.ewt.nicola.ewt.dao

import com.ewt.nicola.ewt.model.Todo
import com.ewt.nicola.realm.extension.asLiveData
import com.ewt.nicola.realm.extension.awaitFirst
import com.ewt.nicola.realm.extension.queryAllAsyncBy
import com.ewt.nicola.realm.util.RealmLiveData
import io.realm.Realm
import io.realm.kotlin.where
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TodoDao(private val realm: Realm) {

    fun getTodo(id: Int): RealmLiveData<Todo> =
        realm.where<Todo>().equalTo("id", id).findAllAsync().asLiveData()

    fun getTodos(): RealmLiveData<Todo> =
        realm.where<Todo>().findAllAsync().asLiveData()

    suspend fun getTodo1() {
        GlobalScope.launch {
            realm.where<Todo>().awaitFirst()
        }
    }
}