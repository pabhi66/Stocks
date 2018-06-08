package com.ap.mobil.stocks.ui.views.recyclerview.adapters

import com.ap.mobil.stocks.R
import com.ap.mobil.stocks.data.local.entity.UserStockList
import com.ap.mobil.stocks.ui.views.recyclerview.base.RecyclerBaseAdapter

/**
 *
 * class information...
 */
class UserStocksListAdapter(
    private val data: List<UserStockList>
): RecyclerBaseAdapter() {
    override fun getViewModel(position: Int): Any? = data[position]

    override fun getLayoutIdForPosition(position: Int): Int = R.layout.recyclerview_stocks_list

    override fun getItemCount(): Int = data.size
}