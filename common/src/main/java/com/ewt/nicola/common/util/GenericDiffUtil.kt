package com.ewt.nicola.common.util

import androidx.recyclerview.widget.DiffUtil
import io.realm.RealmModel
import io.realm.kotlin.isValid

class GenericDiffUtil<T : RealmModel>(private val oldData: List<T>, private val newData: List<T>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]

        return if (oldItem.isValid() && newItem.isValid())
            oldItem == newItem
        else
            false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]

        return if (oldItem.isValid() && newItem.isValid())
            oldItem != newItem
        else
            false
    }

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size
}