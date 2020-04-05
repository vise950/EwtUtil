package com.ewt.nicola.ewt.application

import android.annotation.SuppressLint
import android.app.Application
import io.realm.Realm
import io.realm.RealmConfiguration
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class Init : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var realm: Realm? = null
        private var okHttpClient: OkHttpClient? = null
        private var retrofit: Retrofit? = null

        fun getRealmInstance(): Realm =
            realm?.let {
                it
            } ?: run {
                Realm.getDefaultInstance()
                    .also {
                        realm = it
                    }
            }

        fun getOkHttpClient(): OkHttpClient =
            okHttpClient?.let {
                it
            } ?: run {
                OkHttpClient.Builder()
                    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                    .build()
                    .also {
                        okHttpClient = it
                    }
            }

        fun getRetrofit(): Retrofit =
            retrofit?.let {
                it
            } ?: run {
                Retrofit.Builder()
                    .baseUrl("https://jsonplaceholder.typicode.com/")
                    .client(getOkHttpClient())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .also {
                        retrofit = it
                    }
            }
    }

    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder().deleteRealmIfMigrationNeeded().build())
    }
}