package com.ap.mobil.stocks.ui.views.recyclerview.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import com.ap.mobil.stocks.data.local.entity.UserStockList
import android.view.View
import android.view.ViewGroup
import com.ap.mobil.stocks.R
import android.widget.TextView
import android.widget.Toast
import com.ap.mobil.stocks.ui.main.DetailDialog

/**
 *
 * class information...
 */
class StocksListAdapter(
    private val context: Context,
    private val data: List<UserStockList>,
    private val listener: OnItemClickListener
) : RecyclerView.Adapter<StocksListAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(symbol: String)
    }

    // inflates the row layout from xml when needed
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.recyclerview_stocks_list, parent, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.symbol.text = data[position].symbol
        holder.company.text = data[position].company
    }

    override fun getItemCount(): Int = data.size

    inner class ViewHolder internal constructor(itemView: View, private val listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var symbol: TextView = itemView.findViewById(R.id.rv_symbol)
        var company: TextView = itemView.findViewById(R.id.rv_company)

        init {
            itemView.setOnClickListener(this)
        }


        override fun onClick(view: View) {
            Toast.makeText(context, data[position].symbol, Toast.LENGTH_SHORT).show()
            listener.onItemClick(data[position].symbol)
            //if (mClickListener != null) mClickListener.onItemClick(view, adapterPosition)
        }
    }
}