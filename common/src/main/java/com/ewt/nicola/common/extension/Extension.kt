package com.ewt.nicola.common.extension

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.graphics.Bitmap
import android.os.Handler
import android.util.Base64
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.ewt.nicola.common.BuildConfig
import com.ewt.nicola.common.R
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.reflect.KClass

fun Any?.log(obj: Any? = null) {
    val logger = when (obj) {
        is KClass<*> -> obj.java.simpleName
        is Class<*> -> obj.simpleName
        is String -> obj
        null -> "Default"
        else -> obj.javaClass.simpleName
    }

    val message = when (this) {
        is String? -> this ?: "NullString"
        is Int? -> if (this == null) "NullInt" else "Int: $this"
        is Float? -> if (this == null) "NullFloat" else "Float: $this"
        is Double? -> if (this == null) "NullDouble" else "Double: $this"
        else -> if (this == null) "NullValue" else "Value: $this"
    }
    android.util.Log.e(logger, message)
}

inline fun <reified E> Activity.goto(finished: Boolean = false, clearTask: Boolean = false, block: (Intent) -> Unit = {}) {
    val i = Intent(this, E::class.java)
    if (clearTask) i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    block.invoke(i)
    startActivity(i)
    if (finished) this.finish()
}

inline fun <reified E> Context.goto(block: (Intent) -> Unit = {}) {
    val i = Intent(this, E::class.java)
    block.invoke(i)
    startActivity(i)
}

inline fun <reified E> Fragment.goto(block: (Intent) -> Unit = {}) {
    val i = Intent(this.requireContext(), E::class.java)
    block.invoke(i)
    startActivity(i)
}

inline fun <reified E> Activity.gotoWithFinish(block: (Intent) -> Unit = {}) {
    val i = Intent(this, E::class.java)
    block.invoke(i)
    startActivity(i)
    this.finish()
}

inline fun <reified E> Context.clearTask() {
    val i = Intent(this, E::class.java)
    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
    startActivity(i)
}

fun <T : Any, R : Any> Collection<T?>.whenAllNotNull(block: (List<T>) -> R) {
    if (this.all { it != null }) {
        block(this.filterNotNull())
    }
}

fun <T : Any, R : Any> Collection<T?>.whenAnyNotNull(block: (List<T>) -> R) {
    if (this.any { it != null }) {
        block(this.filterNotNull())
    }
}

fun isDebug() = BuildConfig.DEBUG

fun Context.isTablet(): Boolean = this.resources.getBoolean(R.bool.isTablet)

fun Context.isTabletLandscape(): Boolean = this.resources.getBoolean(R.bool.isTablet) &&
        resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

fun Activity.lockScreenOrientation() {
    this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LOCKED
}

fun Activity.unlockScreenOrientation() {
    this.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED
}

fun Activity.hideKeyboard() {
    (this.getSystemService(Activity.INPUT_METHOD_SERVICE) as? InputMethodManager)?.let {
        this.currentFocus?.windowToken?.let { token ->
            it.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS)
        }
    }
}

fun Any?.toStringOrNull(): String? {
    when (this) {
        is String -> if (this.isEmpty()) return null
        is CharSequence -> if (this.isEmpty()) return null
    }
    return this.toString()
}

fun Any?.toStringOrEmpty(): String {
    return this?.let { it.toString() } ?: run { "--" }
}

inline fun delay(time: Long = 1500, crossinline block: () -> Unit = {}) {
    Handler().postDelayed({ block.invoke() }, time)
}

fun Context.color(color: Int): Int = ContextCompat.getColor(this, color)

fun Bitmap.toBase64(): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    this.compress(Bitmap.CompressFormat.PNG, 70, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun String.toImage(): ByteArray {
    this.replace("data:image/jpg;base64,", "")
    return Base64.decode(this, Base64.DEFAULT)
}

fun uuid(): String = UUID.randomUUID().toString()

fun Date.toIsoFormat(): String {
    val sfd = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return sfd.format(this)
}

fun Context.getSpinnerAdapter(items: List<String>): ArrayAdapter<String> {
    val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items)
    adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
    return adapter
}

inline fun <T> Iterable<T>.forEachOrEmpty(action: (T) -> Unit) {
    if (this.count() != 0)
        for (element in this) action(element)
    else
        return
}