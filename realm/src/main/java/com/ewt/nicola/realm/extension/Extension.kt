package com.ewt.nicola.realm.extension


import com.ewt.nicola.realm.util.RealmLiveData
import com.ewt.nicola.realm.util.RealmPromise
import io.realm.*
import io.realm.exceptions.RealmException
import io.realm.kotlin.deleteFromRealm
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async


fun RealmModel.toRealm(realm: Realm, onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {}) {
    this.save(realm)
        .then { onSuccess.invoke() }
        .onError { onError.invoke(it) }
}

fun RealmList<out RealmModel>.toRealm(
    realm: Realm, removeOld: Boolean,
    onSuccess: () -> Unit = {}, onError: (Throwable) -> Unit = {}
) {
    this.save(realm, removeOld)
        .then { onSuccess.invoke() }
        .onError { onError.invoke(it) }
}

/**
 * Check if realm is not null and not closed
 * If true return the realm, otherwise return null and report the exception to the onError handler of the promise
 **/
fun <T> RealmPromise<T>?.checkRealmStatus(realm: Realm?): Realm? {
    realm?.let {
        return if (it.isClosed) {
            this?.error?.invoke(RealmException("Realm is closed"))
            null
        } else {
            it
        }
    } ?: run {
        this?.error?.invoke(RealmException("Realm is null"))
        return null
    }
}

/**
 * Execute an async transaction in a safe realm
 **/
fun Realm?.safeExec(async: (Realm) -> Unit): RealmPromise<Boolean> {
    val promise = RealmPromise<Boolean>()
    promise.checkRealmStatus(this)?.let { realm ->
        realm.executeTransactionAsync(
            { async(it) },
            { promise.action?.invoke(true) },
            { promise.error?.invoke(it) }
        )
    }
    return promise
}

/**
 * Insert on update object into realm db
 */
fun <E : RealmModel> Realm?.addOrUpdate(obj: E): RealmPromise<E> {
    RealmPromise<E>().let { promise ->
        this.safeExec {
            it.insertOrUpdate(obj)
        } then {
            promise.action?.invoke(obj)
        } onError {
            promise.error?.invoke(it)
        }
        return promise
    }
}

/**
 * Insert or update list into realm db
 */
fun <E : RealmModel> Realm?.addOrUpdate(list: RealmList<E>): RealmPromise<RealmList<E>> {
    RealmPromise<RealmList<E>>().let { promise ->
        this.safeExec {
            it.insertOrUpdate(list)
        } then {
            promise.action?.invoke(list)
        } onError {
            promise.error?.invoke(it)
        }
        return promise
    }
}

/**
 * Save network response (object) on realm
 */
fun <E : RealmModel> E.save(realm: Realm): RealmPromise<E> {
    RealmPromise<E>().let { promise ->
        GlobalScope.async(Dispatchers.Main) {
            realm.addOrUpdate(this@save)
                .then { promise.action?.invoke(it) }
                .onError { promise.error?.invoke(it) }
        }
        return promise
    }
}

/**
 * Save network response (list) on realm
 * @param removeOld remove not received object from response from realm
 */
fun <E : RealmList<out RealmModel>> E.save(realm: Realm, removeOld: Boolean): RealmPromise<E> {
    RealmPromise<E>().let { promise ->
        GlobalScope.async(Dispatchers.Main) {
            if (removeOld) {
                val items = this@save.firstOrNull()?.let { realm.where(it::class.java).findAll() }
                this@save.forEach {
                    if (items?.contains(it) == false) it.deleteFromRealm()
                }
            }
            realm.addOrUpdate(this@save)
                .then {
                    promise.action?.invoke(it as E)
                }
                .onError { promise.error?.invoke(it) }
        }
        return promise
    }
}


/**
 *  UTILS
 */

fun <T : RealmModel> RealmResults<T>.asLiveData() = RealmLiveData(this)

fun List<String>.toRealmList(): RealmList<String> {
    val realmList = RealmList<String>()
    this.forEach { if (it.isNotEmpty()) realmList.add(it) }
    return realmList
}

fun <T : RealmObject> RealmQuery<T>.queryBy(id: String, fieldName: String = "id"): T? =
    this.equalTo(fieldName, id).findFirst()

fun <T : RealmObject> RealmQuery<T>.queryAllBy(id: String, fieldName: String = "id"): RealmResults<T> =
    this.equalTo(fieldName, id).findAll()

fun <T : RealmObject> RealmQuery<T>.queryAsyncBy(id: String, fieldName: String = "id"): T? =
    this.equalTo(fieldName, id).findFirstAsync()

fun <T : RealmObject> RealmQuery<T>.queryAllAsyncBy(id: String, fieldName: String = "id"): RealmResults<T> =
    this.equalTo(fieldName, id).findAllAsync()

fun <T : RealmObject> RealmQuery<T>.queryAllAsync(): RealmResults<T> =
    this.findAllAsync()
