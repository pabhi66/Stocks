package com.ap.mobile.stocks.ui.views.rxrecyclerview

import android.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import com.ap.mobile.stocks.data.repository.StocksRepository
import com.jakewharton.rxbinding2.view.clicks
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject

/**
 * A RecyclerView adapter which uses data binding for items.
 * @param observable the observable which provides values
 * @param initialValue the initial value
 * @param inflate the item binding inflater (ie ItemBinding::inflate)
 * @param set the item binding setter generated by the data binding from variable tag (ie ItemBinding::setItem)
 * @param id the item method which provides the id used as stable id (see setHasStableIds) (ie Item::id)
 */
@Suppress("unused")
open class RxSimpleAdapter<T : Any, U : ViewDataBinding> @Inject constructor(
    observable: Observable<List<T>>,
    private val initialValue: List<T>,
    private val inflate: (LayoutInflater, ViewGroup, Boolean) -> U,
    private val set: U.(T) -> Unit,
    private val id: (T.() -> Long)? = null,
    isUserStockList: Boolean? = null,
    stocksRepository: StocksRepository? = null
) : RxRecyclerAdapter<List<T>, BindingHolder<U>>(observable, initialValue, isUserStockList, stocksRepository){

    @Suppress("MemberVisibilityCanBePrivate")

        /**
         * A subject which sends clicked items.
         */
    val clicks: PublishSubject<T> = PublishSubject.create()

    init {
        @Suppress("LeakingThis")
        setHasStableIds(id != null)
    }

    override fun getItemId(position: Int) = id?.invoke(value[position]) ?: 0

    override fun getItemCount(): Int = value.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = BindingHolder(inflate(LayoutInflater.from(parent.context), parent, false)).apply {
        itemView.clicks()
            .filter { adapterPosition in value.indices }
            .map { value[adapterPosition] }
            .bindToLifecycle(parent).subscribe(clicks)
    }

    override fun onBindViewHolder(holder: BindingHolder<U>, position: Int) {
        holder.binding.set(value[position])
        holder.binding.executePendingBindings()
    }
}
