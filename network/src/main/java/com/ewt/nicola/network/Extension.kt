package com.ewt.nicola.network

import com.ewt.nicola.common.extension.launch
import kotlinx.coroutines.Deferred
import retrofit2.Response

/**
 *
 */
fun <T> Deferred<Response<T>>.get(onSuccess: (T) -> Unit = {}, onError: (Throwable) -> Unit) {
    this.awaitAsync {
        if (it.isSuccessful) {
            onSuccess.invoke(it.body()!!)
        } else {
            onError.invoke(Throwable(it.errorBody()?.string()))
        }
    }
}

fun <T> Deferred<Response<T>>.awaitAsync(block: (Response<T>) -> Unit) {
    launch {
        this@awaitAsync.await().let { block.invoke(it) }
    }
}