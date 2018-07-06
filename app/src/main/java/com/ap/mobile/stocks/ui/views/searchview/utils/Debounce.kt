package com.ap.mobile.stocks.ui.views.searchview.utils

import android.util.Log
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/17/18.
 */
/**
 * The debouncer base
 * Implements everything except for the callback,
 * as the number of variables is different between implementations
 * You may still use this without extending it, but you'll have to pass a callback each time
 */
open class Debouncer(var interval: Long) {
    private val sched = Executors.newScheduledThreadPool(1)
    private var task: DebounceTask? = null

    /**
     * Generic invocation to pass a callback to the new task
     * Pass a new callback for the task
     * If another task is pending, it will be invalidated
     */
    operator fun invoke(callback: () -> Unit) {
        synchronized(this) {
            task?.invalidate()
            val newTask = DebounceTask(callback)
            Log.v (Log.VERBOSE.toString(),"Debouncer task created: $newTask in $this" )
            sched.schedule(newTask, interval, TimeUnit.MILLISECONDS)
            task = newTask
        }
    }

    /**
     * Call to cancel all pending requests and shutdown the thread pool
     * The debouncer cannot be used after this
     */
    fun terminate() = sched.shutdownNow()

    /**
     * Invalidate any pending tasks
     */
    fun cancel() {
        synchronized(this) {
            if (task != null) Log.v (Log.VERBOSE.toString(),"Debouncer cancelled for $task in $this" )
            task?.invalidate()
            task = null
        }
    }

}

/*
 * Helper extensions for functions with 0 to 3 arguments
 */

/**
 * The debounced task
 * Holds a callback to execute if the time has come and it is still valid
 * All methods can be viewed as synchronous as the invocation is synchronous
 */
private class DebounceTask(inline val callback: () -> Unit) : Runnable {
    private var valid = true

    fun invalidate() {
        valid = false
    }

    override fun run() {
        if (!valid) return
        valid = false
        Log.v (Log.VERBOSE.toString(),"Debouncer task executed $this" )
        try {
            callback()
        } catch (e: Exception) {
            Log.v (Log.VERBOSE.toString(),"DebouncerTask exception" )
        }
    }
}

/**
 * A zero input debouncer
 */
class Debouncer0 internal constructor(interval: Long, val callback: () -> Unit) : Debouncer(interval) {
    operator fun invoke() = invoke(callback)
}

fun debounce(interval: Long, callback: () -> Unit) = Debouncer0(interval, callback)
fun (() -> Unit).debounce(interval: Long) = debounce(interval, this)

/**
 * A one argument input debouncer
 */
class Debouncer1<T> internal constructor(interval: Long, val callback: (T) -> Unit) : Debouncer(interval) {
    operator fun invoke(key: T) = invoke { callback(key) }
}

fun <T> debounce(interval: Long, callback: (T) -> Unit) = Debouncer1(interval, callback)
fun <T> ((T) -> Unit).debounce(interval: Long) = debounce(interval, this)

/**
 * A two argument input debouncer
 */
class Debouncer2<T, V> internal constructor(interval: Long, val callback: (T, V) -> Unit) : Debouncer(interval) {
    operator fun invoke(arg0: T, arg1: V) = invoke { callback(arg0, arg1) }
}

fun <T, V> debounce(interval: Long, callback: (T, V) -> Unit) = Debouncer2(interval, callback)
fun <T, V> ((T, V) -> Unit).debounce(interval: Long) = debounce(interval, this)

/**
 * A three argument input debouncer
 */
class Debouncer3<T, U, V> internal constructor(interval: Long, val callback: (T, U, V) -> Unit) : Debouncer(interval) {
    operator fun invoke(arg0: T, arg1: U, arg2: V) = invoke { callback(arg0, arg1, arg2) }
}

fun <T, U, V> debounce(interval: Long, callback: ((T, U, V) -> Unit)) = Debouncer3(interval, callback)
fun <T, U, V> ((T, U, V) -> Unit).debounce(interval: Long) = debounce(interval, this)
