package com.ewt.nicola.common.extension

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders


inline fun <reified T : ViewModel> AppCompatActivity.viewModel(crossinline f: () -> T): T {
    return ViewModelProviders.of(this, factory(f)).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.viewModel(crossinline f: () -> T): T {
    return ViewModelProviders.of(this, factory(f)).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.viewModel(crossinline f: () -> T): T {
    return ViewModelProviders.of(this, factory(f)).get(T::class.java)
}

inline fun <VM : ViewModel> factory(crossinline f: () -> VM) = object : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return f() as T
    }
}