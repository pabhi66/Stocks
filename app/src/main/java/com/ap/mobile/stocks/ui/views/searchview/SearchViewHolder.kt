package com.ap.mobile.stocks.ui.views.searchview

import android.view.MenuItem

/**
 * Interface to help facilitate searchview binding and actions
 */
interface SearchViewHolder {

    var searchView: SearchView?

    fun searchViewBindIfNull(binder: () -> SearchView) {
        if (searchView == null) searchView = binder()
    }

    fun searchViewOnBackPress() = searchView?.onBackPressed() ?: false

    fun searchViewUnBind(replacementMenuItemClickListener: ((item: MenuItem) -> Boolean)? = null) {
        searchView?.unBind(replacementMenuItemClickListener)
        searchView = null
    }

}