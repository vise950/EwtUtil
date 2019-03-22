package com.ewt.nicola.ewt.model

import io.realm.RealmObject
import io.realm.annotations.PrimaryKey

open class User(
    @PrimaryKey
    var id: Int? = null,
    var userId: Int? = null,
    var title: String? = null,
    var completed: Boolean? = null
) : RealmObject() {
    override fun toString(): String {
        return "id: $userId , title: $title"
    }
}