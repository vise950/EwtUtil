package com.ewt.nicola.ewt.util

import com.ewt.nicola.ewt.dao.TodoDao
import io.realm.Realm

fun Realm.todoDao(): TodoDao = TodoDao(this)
