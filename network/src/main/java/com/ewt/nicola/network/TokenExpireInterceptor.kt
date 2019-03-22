package com.ewt.nicola.network

import okhttp3.Interceptor
import okhttp3.Response


class TokenExpireInterceptor(private val block: (() -> Unit)) : Interceptor {

    companion object {
        const val UNAUTHORIZED_CODE = 401
        const val UNAUTHORIZED_MESSAGE = "Unauthorized"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        if (response.code() == UNAUTHORIZED_CODE && response.message() == UNAUTHORIZED_MESSAGE) {
            block.invoke()
        }
        return response
    }
}