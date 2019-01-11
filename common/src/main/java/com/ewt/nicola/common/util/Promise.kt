package com.ewt.nicola.common.util

class Promise<T> {
     var action: ((T) -> Unit)? = null
     var error: ((Throwable) -> Unit)? = null

    infix fun then(promise: (T) -> Unit): Promise<T> {
        action = promise
        return this
    }

    infix fun error(promise: (Throwable) -> Unit): Promise<T> {
        error = promise
        return this
    }
}