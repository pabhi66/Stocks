package com.ap.mobil.stocks.ui.main

import android.app.Dialog
import android.os.Bundle
import android.support.design.widget.BottomSheetDialogFragment
import com.ap.mobil.stocks.R

/**
 *
 * class information...
 */
class DetailDialog: BottomSheetDialogFragment() {

    companion object {
        private const val SYMBOL = "symbol"
        fun newInstance(symbol: String) = DetailDialog().apply {
            arguments = Bundle(1).apply {
                putString(SYMBOL, symbol)
            }
        }
    }

    init {

    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        dialog?.apply {
            setContentView(R.layout.dialog_stock_detail)
        }
    }
}