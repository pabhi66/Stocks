package com.ap.mobile.stocks.ui.views.rxrecyclerview

import android.support.v7.widget.RecyclerView
import android.util.Log
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable

/**
 * A RecyclerView adapter which updates when a new value arrives.
 * @param observable the observable which provides values
 * @param initialValue the initial value
 */
@Suppress("MemberVisibilityCanBePrivate")
abstract class RxRecyclerAdapter<T : Any, U : RecyclerView.ViewHolder>(
    val observable: Observable<T>,
    initialValue: T
) : RecyclerView.Adapter<U>() {
    /**
     * The current value.
     */
    var value = initialValue
        private set
    private var dispose: Disposable? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        dispose = observable.observeOn(AndroidSchedulers.mainThread()).bindToLifecycle(recyclerView).subscribe {
            value = it
            notifyDataSetChanged()
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        dispose?.dispose()
        dispose = null
    }
}