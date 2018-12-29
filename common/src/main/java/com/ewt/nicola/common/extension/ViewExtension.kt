package com.ewt.nicola.common.extension

import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.ewt.nicola.common.R

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.disable() {
    isEnabled = false
    if (this is ViewGroup) {
        for (idx in 0 until childCount) {
            getChildAt(idx).disable()
        }
    }
}

fun View.enable() {
    isEnabled = true
    if (this is ViewGroup) {
        for (idx in 0 until childCount) {
            getChildAt(idx).enable()
        }
    }
}

fun RecyclerView.runLayoutAnimation() {
    val controller = AnimationUtils.loadLayoutAnimation(this.context,
        R.anim.layout_animation_fall_down
    )
    this.apply {
        layoutAnimation = controller
        adapter?.notifyDataSetChanged()
        scheduleLayoutAnimation()
    }
}