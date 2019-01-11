package com.ewt.nicola.ewt.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.ewt.nicola.ewt.R
import com.ewt.nicola.ewt.application.Init
import com.ewt.nicola.ewt.service.Service
import com.ewt.nicola.network.get
import com.ewt.nicola.realm.extension.save
import kotlinx.android.synthetic.main.activity_1.*

class Activity1 : AppCompatActivity() {

    private val TAG = "ACTIVITY_1"

    private val realm = Init.getRealmInstance()
    private val service = Init.getRetrofit()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        btn_1.setOnClickListener { fetchTodo() }
        btn_2.setOnClickListener { fetchTodo1() }
        btn_3.setOnClickListener { fetchTodos() }
    }

    private fun fetchTodo() {
        service.create(Service::class.java)
            .getTodo()
            .get({
                Log.e(TAG, it.toString())
            }, {
                Log.e(TAG, "error ", it)
            })
    }

    private fun fetchTodo1() {
        service.create(Service::class.java)
            .getTodo()
            .get({
                it.save(realm)
                    .onError { Log.e(TAG, "error ", it) }
            }, {
                Log.e(TAG, "error ", it)
            })
    }

    private fun fetchTodos() {
        service.create(Service::class.java)
            .getTodos()
            .get({
                Log.e(TAG, "get and saved")
                it.save(realm, false)
                    .onError { Log.e(TAG, "error ", it) }
            }, {
                Log.e(TAG, "error ", it)
            })
    }
}