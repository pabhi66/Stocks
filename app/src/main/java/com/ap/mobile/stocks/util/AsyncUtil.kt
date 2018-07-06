package com.ap.mobile.stocks.util

import android.content.Context
import android.os.Handler
import android.os.Looper

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/17/18.
 */

private object ContextHelper {
    val handler = Handler(Looper.getMainLooper())
    val mainThread: Thread = Looper.getMainLooper().thread
}

/**
 * Execute [f] on the application UI thread.
 */
fun Context.runOnUiThread(f: Context.() -> Unit) {
    if (ContextHelper.mainThread == Thread.currentThread()) f() else ContextHelper.handler.post { f() }
}