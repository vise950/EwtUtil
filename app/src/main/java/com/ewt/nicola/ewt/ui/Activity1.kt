package com.ewt.nicola.ewt.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.observe
import com.ewt.nicola.common.extension.log
import com.ewt.nicola.common.extension.viewModel
import com.ewt.nicola.ewt.R
import com.ewt.nicola.ewt.viewmodel.TodoViewModel
import kotlinx.android.synthetic.main.activity_1.*

class Activity1 : AppCompatActivity() {

    private val todoViewModel by lazy { viewModel { TodoViewModel(this) } }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_1)

        observeData(1)
        initView()
    }

    private fun initView(){
        btn_1.setOnClickListener { fetchTodo("1") }
        btn_2.setOnClickListener {  }
    }

    private fun observeData(id:Int){
        todoViewModel.getTodo(id).observe(this) {
            it.first()?.title.log("todo $id:")
        }
    }

    private fun fetchTodo(id: String) {
        todoViewModel.fetchTodo(id)

//        todoViewModel.todo1.observe(this) {
//            it?.id.log("todo1 id")
//        }
//
//
//        todoViewModel.todo3.observe(this) {
//            it.id.log("todo2 id")
//        }
//
//        todoViewModel.todo4.observe(this) {
//            //it?.id.log("todo3 id")
//        }

    }
}