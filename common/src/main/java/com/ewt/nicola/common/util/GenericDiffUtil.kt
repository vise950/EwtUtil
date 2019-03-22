package com.ewt.nicola.common.util

import androidx.recyclerview.widget.DiffUtil


class GenericDiffUtil<T : Any>(private val oldData: List<T>, private val newData: List<T>) :
    DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]

        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData[oldItemPosition]
        val newItem = newData[newItemPosition]

        return oldItem != newItem
    }

    override fun getOldListSize(): Int = oldData.size

    override fun getNewListSize(): Int = newData.size
}