package com.ap.mobile.stocks.ui.views.rxrecyclerview

import android.databinding.ViewDataBinding
import android.support.v7.widget.RecyclerView

/**
 * A ViewHolder which contains the binding.
 */
class BindingHolder<out T : ViewDataBinding>(val binding: T) : RecyclerView.ViewHolder(binding.root)