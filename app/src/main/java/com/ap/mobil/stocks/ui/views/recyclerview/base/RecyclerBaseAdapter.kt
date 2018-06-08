package com.ap.mobil.stocks.ui.views.recyclerview.base

import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import com.android.databinding.library.baseAdapters.BR
/**
 *
 * Base recycler view adapter
 */
abstract class RecyclerBaseAdapter: RecyclerView.Adapter<RecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        RecyclerViewHolder(DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), viewType, parent, false))

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        getViewModel(position)
            ?.let {
                val bindingSuccess = holder.binding.setVariable(BR.viewModel, it)
                if (!bindingSuccess) {
                    throw IllegalStateException("Binding ${holder.binding} viewModel variable name should be 'viewModel'")
                }
            }
    }

    abstract fun getViewModel(position: Int): Any?

    override fun getItemViewType(position: Int) = getLayoutIdForPosition(position)

    abstract fun getLayoutIdForPosition(position: Int): Int
}