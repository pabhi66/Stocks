package com.ap.mobile.stocks.ui.views.rxrecyclerview

import android.support.v7.widget.RecyclerView
import com.ap.mobile.stocks.data.local.entity.Quote
import com.ap.mobile.stocks.data.local.entity.UserStockList
import com.ap.mobile.stocks.data.repository.StocksRepository
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import retrofit2.Call
import retrofit2.Response
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * A RecyclerView adapter which updates when a new value arrives.
 * @param observable the observable which provides values
 * @param initialValue the initial value
 */
@Suppress("MemberVisibilityCanBePrivate", "UNCHECKED_CAST")
abstract class RxRecyclerAdapter<T : Any, U : RecyclerView.ViewHolder>(
    val observable: Observable<T>,
    initialValue: T,
    val isUserStockList: Boolean?,
    val stocksRepository: StocksRepository?
) : RecyclerView.Adapter<U>() {
    /**
     * The current value.
     */
    var value = initialValue
        private set
    private var dispose: Disposable? = null

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        dispose = observable.observeOn(AndroidSchedulers.mainThread()).bindToLifecycle(recyclerView).subscribe {
            if(isUserStockList != null) {
                if(!isUserStockList) {
                    value = it
                }
                else {
                    val list = it as List<UserStockList>
                    list.forEach {

                        stocksRepository?.getQuote(it.symbol)?.enqueue(object: retrofit2.Callback<Quote> {
                            override fun onFailure(call: Call<Quote>?, t: Throwable?) {
                                it.price = "-"
                            }

                            override fun onResponse(call: Call<Quote>?, response: Response<Quote>?) {
                                val quote = response?.body()
                                val df = DecimalFormat("#.##")
                                df.roundingMode = RoundingMode.CEILING
                                val currentPrice = quote?.latestPrice
                                if(currentPrice != null) {
                                    it.price = df.format(currentPrice)
                                    val change = quote.changePercent
                                    if(change != null) {
                                        if(change < 0) {
                                            it.priceColor = -1
                                        } else {
                                            it.priceColor = 1
                                        }
                                    }
                                } else {
                                    it.price = "-"
                                }

                                value = list as T
                                notifyDataSetChanged()
                            }
                        })
                    }
                }
            } else {
                value = it
            }
            notifyDataSetChanged()
        }
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        dispose?.dispose()
        dispose = null
    }
}