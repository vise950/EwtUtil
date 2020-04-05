package com.ewt.nicola.realm.extension


import com.ewt.nicola.common.extension.asyncUI
import com.ewt.nicola.common.extension.log
import com.ewt.nicola.common.util.Promise
import com.ewt.nicola.realm.util.RealmLiveData
import io.realm.*
import io.realm.exceptions.RealmException
import io.realm.kotlin.deleteFromRealm
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

/**
 * Check if realm is not null and not closed
 * If true return the realm, otherwise return null and report the exception to the onError handler of the promise
 **/
fun <T> Promise<T>?.checkRealmStatus(realm: Realm?): Realm? {
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
fun Realm?.safeExec(async: (Realm) -> Unit): Promise<Boolean> {
    val promise = Promise<Boolean>()
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
fun <E : RealmModel> Realm?.addOrUpdate(obj: E): Promise<E> {
    Promise<E>().let { promise ->
        this.safeExec {
            it.insertOrUpdate(obj)
        } then {
            promise.action?.invoke(obj)
        } error {
            promise.error?.invoke(it)
        }
        return promise
    }
}

/**
 * Insert or update list into realm db
 */
fun <E : RealmModel> Realm?.addOrUpdate(list: RealmList<E>): Promise<RealmList<E>> {
    Promise<RealmList<E>>().let { promise ->
        this.safeExec {
            it.insertOrUpdate(list)
            "insert list".log()
        } then {
            promise.action?.invoke(list)
            "insert list OK".log()
        } error {
            promise.error?.invoke(it)
            "insert list KO".log()
        }
        return promise
    }
}

/**
 * Save network response (object) on realm
 */
fun <E : RealmModel> E.save(realm: Realm, beforeSave: ((E) -> Unit)? = null): Promise<E> {
    Promise<E>().let { promise ->
        asyncUI {
            beforeSave?.invoke(this@save)
            realm.addOrUpdate(this@save)
                .then { promise.action?.invoke(it) }
                .error { promise.error?.invoke(it) }
        }
        return promise
    }
}

/**
 * Save network response (list) on realm
 * @param removeInvalid remove not received objects in the response from realm db
 */
fun <E : RealmList<out RealmModel>> E.save(
    realm: Realm,
    beforeSave: ((E) -> Unit)? = null,
    removeInvalid: Boolean = false
): Promise<E> {
    Promise<E>().let { promise ->
        asyncUI {
            beforeSave?.invoke(this@save)
            if (removeInvalid) this@save.removeInvalidObject(realm)
            "finish remove old object".log()
            //fixme remove old work only first time when db is empty
            realm.addOrUpdate(this@save)
                .then {
                    promise.action?.invoke(it as E)
                }
                .error { promise.error?.invoke(it) }
        }
        return promise
    }
}

fun <E : RealmList<out RealmModel>> E.removeInvalidObject(realm: Realm) {
    val items = this.firstOrNull()?.let { realm.where(it::class.java).findAll() }
    items?.forEach {
        "foreach".log()
        if (!this.contains(it as RealmModel)) {
            "remove old object".log()
            it.deleteFromRealm()
        } else
            return@forEach
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


private suspend fun <T: RealmObject, S: RealmQuery<T>> findAllAwait(query: S): RealmResults<T> = suspendCancellableCoroutine { continuation ->
    val listener = RealmChangeListener<RealmResults<T>> { t -> continuation.resume(t) }
    query.findAllAsync().addChangeListener(listener)
}

private suspend fun <T: RealmObject, S: RealmQuery<T>> findFirstAwait(query: S): T? = suspendCancellableCoroutine { continuation ->
    val listener = RealmChangeListener { t: T? -> continuation.resume(t) }
    query.findFirstAsync().addChangeListener(listener)
}

private suspend fun executeAsync(realm: Realm, block: (Realm) -> Unit): Unit = suspendCancellableCoroutine { continuation ->
    realm.executeTransactionAsync({ block(it)  }, { continuation.resume(Unit) }, { continuation.resumeWithException(it) })
}

suspend fun <S: RealmObject> RealmQuery<S>.await() = findAllAwait(this)

suspend fun <S: RealmObject> RealmQuery<S>.awaitFirst() = findFirstAwait(this)

suspend fun Realm.transactAwait(block: (Realm) -> Unit) =  executeAsync(this, block)