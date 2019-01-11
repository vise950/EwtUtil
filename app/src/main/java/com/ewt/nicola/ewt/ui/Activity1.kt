package com.ewt.nicola.ewt.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ewt.nicola.common.extension.log
import com.ewt.nicola.ewt.R
import com.ewt.nicola.ewt.application.Init
import com.ewt.nicola.ewt.service.Service
import com.ewt.nicola.network.get
import com.ewt.nicola.realm.extension.save
import kotlinx.android.synthetic.main.activity_1.*

class Activity1 : AppCompatActivity() {

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
            .get()
            .then { it.toString().log() }
            .error { it.log("fetchTodo") }
    }

    private fun fetchTodo1() {
        service.create(Service::class.java)
            .getTodo()
            .get()
            .then { it.save(realm) }
            .error { it.log("fetchTodo1") }
    }

    private fun fetchTodos() {
        service.create(Service::class.java)
            .getTodos()
            .get()
            .then { it.save(realm, false) }
            .error { it.log("fetchTodoa") }
    }
}