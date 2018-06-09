package com.ap.mobile.stocks.ui.views.recyclerview

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
import com.jakewharton.rxbinding2.view.clicks
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.subjects.PublishSubject

/**
 * Generic recycler view adapter
 * Just pass in the List<T> data and the layout resource.
 */
@Suppress("MemberVisibilityCanBePrivate", "unused")
class RecyclerViewAdapter<T: Any>(
    private var data: List<T>,
    @LayoutRes private val resource: Int
): RecyclerView.Adapter<RecyclerViewHolder>() {
    /**
     * A subject which sends clicked items.
     */
    val clicks: PublishSubject<T> = PublishSubject.create()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(parent.context),
            viewType,
            parent,
            false)).apply {
            itemView.clicks()
                .filter { adapterPosition in data.indices }
                .map { data[adapterPosition] }
                .bindToLifecycle(parent).subscribe(clicks)
        }


    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        getViewModel(position)
            ?.let {
                val bindingSuccess = holder.binding.setVariable(BR.viewModel, it)
                if (!bindingSuccess) {
                    throw IllegalStateException("Binding ${holder.binding} viewModel variable name should be 'viewModel'")
                }
            }
        holder.binding.executePendingBindings()
    }

    private fun getViewModel(position: Int): Any? = data[position]

    override fun getItemViewType(position: Int) = resource

    override fun getItemCount(): Int = data.size
}