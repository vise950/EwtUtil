package com.ewt.nicola.network

import com.ewt.nicola.common.extension.launch
import com.ewt.nicola.common.util.Promise
import kotlinx.coroutines.Deferred
import retrofit2.Response

/**
 *
 */
fun <T> Deferred<Response<T>>.get(): Promise<T> {
    val promise = Promise<T>()
    this.awaitAsync {
        if (it.isSuccessful) {
            promise.action?.invoke(it.body()!!)
        } else {
            promise.error?.invoke(Throwable(it.errorBody().toString()))
        }
    }
    return promise
}

private fun <T> Deferred<Response<T>>.awaitAsync(block: (Response<T>) -> Unit) {
    launch {
        this@awaitAsync.await().let { block.invoke(it) }
    }
}



fun <T> Response<T>.make(): Promise<T> {
    val promise = Promise<T>()
    if (this.isSuccessful) {
        promise.action?.invoke(this.body()!!)
    } else {
        promise.error?.invoke(Throwable(this.errorBody().toString()))
    }
    return promise
}