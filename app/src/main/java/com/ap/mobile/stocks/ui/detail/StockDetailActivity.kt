package com.ap.mobile.stocks.ui.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.databinding.ActivityStockDetailBinding
import com.ap.mobile.stocks.ui.base.BaseActivity

class StockDetailActivity : BaseActivity<ActivityStockDetailBinding>() {

    companion object {
        private const val SYMBOL = "symbol"
        fun newIntent(context: Context, symbol:String): Intent {
            val intent = Intent(context, StockDetailActivity::class.java)
            intent.putExtra(SYMBOL, symbol)
            return intent
        }
    }

    override fun getLayoutRes(): Int = R.layout.activity_stock_detail

}
