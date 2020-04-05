package com.ewt.nicola.ewt.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class Todo(
    @PrimaryKey
    var id: Int? = null,
    var title: String? = null
) : RealmObject() {
    override fun toString(): String {
        return "id: $id , title: $title"
    }
}