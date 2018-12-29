package com.ewt.nicola.network

import android.util.Log
import com.ewt.nicola.realm.save
import io.realm.Realm
import io.realm.RealmList
import io.realm.RealmModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 *
 */
fun <T> Deferred<Response<T>>.get(onSuccess: (T) -> Unit, onError: (Throwable) -> Unit) {
    GlobalScope.launch {
        this@get.await().let {
            if (it.isSuccessful) {
                onSuccess.invoke(it.body()!!)
            } else {
                onError.invoke(Throwable(it.errorBody()?.string()))
            }
        }
    }
}

/**
 *
 */
fun <T : RealmModel> Deferred<Response<T>>.getAndAddObjectToRealm(
    realm: Realm,
    onSuccess: () -> Unit,
    onError: (Throwable) -> Unit
) {
    GlobalScope.launch {
        this@getAndAddObjectToRealm.await().let {
            if (it.isSuccessful) {
                it.body()!!.save(realm)
                    .then {
                        onSuccess.invoke()
                    }
                    .onError { onError.invoke(it) }
            } else {
                onError.invoke(Throwable(it.errorBody()?.string()))
            }
        }
    }
}

/**
 *
 */
fun <T : RealmList<out RealmModel>> Deferred<Response<T>>.getAndAddListToRealm(
    realm: Realm,
    removeOld: Boolean,
    onSuccess: () -> Unit,
    onError: (Throwable) -> Unit
) {
    GlobalScope.launch {
        this@getAndAddListToRealm.await().let {
            if (it.isSuccessful) {
                it.body()!!.save(realm, removeOld)
                    .then {
                        onSuccess.invoke()
                    }
                    .onError { onError.invoke(it) }
            } else {
                onError.invoke(Throwable(it.errorBody()?.string()))
            }
        }
    }
}

