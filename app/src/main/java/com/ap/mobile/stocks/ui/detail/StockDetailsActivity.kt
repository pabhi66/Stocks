package com.ap.mobile.stocks.ui.detail

import android.arch.lifecycle.Observer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.support.design.widget.AppBarLayout
import android.support.v4.view.ViewCompat
import android.view.View
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.data.local.entity.NewsItem
import com.ap.mobile.stocks.data.local.entity.Stock
import com.ap.mobile.stocks.data.local.entity.chart.Chart
import com.ap.mobile.stocks.databinding.ActivityStockDetailsBinding
import com.ap.mobile.stocks.databinding.RecyclerviewStockNewsListBinding
import com.ap.mobile.stocks.ui.base.BaseActivityWithVM
import com.ap.mobile.stocks.ui.main.NetworkViewModel
import com.ap.mobile.stocks.ui.views.rxrecyclerview.RxSimpleAdapter
import com.ap.mobile.stocks.util.*
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import com.github.mikephil.charting.utils.MPPointF
import com.trello.rxlifecycle2.kotlin.bindToLifecycle
import io.reactivex.Observable
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

@Suppress("SetTextI18n","UNCHECKED_CAST", "NAME_SHADOWING")
class StockDetailsActivity : BaseActivityWithVM<NetworkViewModel, ActivityStockDetailsBinding>(), AppBarLayout.OnOffsetChangedListener {

    private lateinit var symbol: String
    private lateinit var company: String

    override fun getViewModel(): Class<NetworkViewModel> = NetworkViewModel::class.java
    override fun getLayoutRes(): Int = R.layout.activity_stock_details

    companion object {
        private const val PERCENTAGE_TO_SHOW_IMAGE = 50
        private const val SYMBOL = "symbol"
        private const val COMPANY = "company"

        fun newIntent(context: Context, symbol:String, company: String): Intent {
            val intent = Intent(context, StockDetailsActivity::class.java)
            intent.putExtra(SYMBOL, symbol)
            intent.putExtra(COMPANY, company)
            return intent
        }
    }

    private var mMaxScrollSize: Int = 0
    private var mIsImageHidden: Boolean = false
    private var isFav = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        symbol = intent.getStringExtra(SYMBOL)
        company = intent.getStringExtra(COMPANY)

        dataBinding.toolbar.setNavigationOnClickListener { onBackPressed() }
        dataBinding.appbar.addOnOffsetChangedListener(this)
        dataBinding.collapsing.setCollapsedTitleTextColor(this.color(R.color.grey1))
        dataBinding.collapsing.setExpandedTitleColor(this.color(R.color.grey1))
        dataBinding.collapsing.setCollapsedTitleTypeface(this.font(R.font.comfortaa_light))
        dataBinding.collapsing.setExpandedTitleTypeface(this.font(R.font.comfortaa_light))

        setupFab()
        loadData()
    }

    override fun onResume() {
        super.onResume()
        // check if there is internet access
        if (!NetworkUtil.isInternetConnected(this)) {
            this.alert("You are not connected to the Internet. Please check your connection and try again.")
        }
    }

    private fun loadData() {
        dataBinding.stockDetailSymbol.text = symbol
        dataBinding.stockDetailCompany.text = company
        viewModel.getStockData(symbol).observe(this, Observer {
            if(it == null) return@Observer
            setupTopData(it)
            setupNews(it)
            setupStats(it)
            setupAbout(it)
            setupFinancial(it)
            setupGraph(it)
            setupEarnings(it)
        })
    }

    private fun setupTopData(stock: Stock) {
        val quote = stock.quote ?: return
        dataBinding.stockDetailPrice.text = "$" + quote.iexRealtimePrice?.format(quote.iexRealtimePrice)
        dataBinding.stockDetailPercentage.text = "$" + quote.change?.format(quote.change) + " (" + quote.changePercent?.format(quote.changePercent) + "%)"
        if(quote.changePercent!! < 0) {
            dataBinding.stockDetailPercentage.setTextColor(this.color(R.color.red))
            dataBinding.stockDetailPrice.setTextColor(this.color(R.color.red))
            dataBinding.triangle.setImageDrawable(this.drawable(R.drawable.ic_triangle_green))
            dataBinding.triangle.rotation = 180f
            dataBinding.triangle.setColorFilter(this.color(R.color.red))
        } else {
            dataBinding.stockDetailPercentage.setTextColor(this.color(R.color.green))
            dataBinding.stockDetailPrice.setTextColor(this.color(R.color.green))
            dataBinding.triangle.setImageDrawable(this.drawable(R.drawable.ic_triangle_green))
        }
        var x = "M"
        val div: Int
        if(quote.marketCap!! > 1000000000) {
            div = 1000000000
            x = "B"
        } else {
            div =1000000
        }
        dataBinding.marketCap.text = quote.marketCap.toDouble().format(quote.marketCap.toDouble()/div) + x
        dataBinding.volume.text = NumberFormat.getNumberInstance(Locale.US).format(quote.latestVolume)
    }

    private fun setupNews(stock: Stock) {
        val news: List<NewsItem>?
        if(stock.news != null) {
            if(stock.news.isNotEmpty()) {
                news = stock.news as List<NewsItem>?
                dataBinding.news!!.stockDetailRv.apply {
                    adapter = RxSimpleAdapter<NewsItem, RecyclerviewStockNewsListBinding>(
                            Observable.fromArray(news),
                            news!!,
                            RecyclerviewStockNewsListBinding::inflate,
                            RecyclerviewStockNewsListBinding::setNews
                    ).apply {
                        clicks.bindToLifecycle(rootView).subscribe {
                            // Toast.makeText(context, "${it.url}", Toast.LENGTH_SHORT).show()
                            dataBinding.news?.webview?.visibility = View.VISIBLE
                            dataBinding.news?.webview?.loadUrl(it.url)
                        }
                    }
                }
            }
        }
    }

    private fun setupStats(stock: Stock) {
        val quote = stock.quote
        dataBinding.stats!!.stockDetailOpenValue.text = quote?.open.toString()
        dataBinding.stats!!.stockDetailHighValue.text = quote?.high.toString()
        dataBinding.stats!!.stockDetailLowValue.text = quote?.low.toString()
        if(quote?.week52High != null) {
            dataBinding.stats!!.stockDetail52highValue.text = quote.week52High.format(quote.week52High)
        } else {
            dataBinding.stats!!.stockDetail52highValue.text = "-"
        }
        if(quote?.week52Low != null) {
            dataBinding.stats!!.stockDetail52lowValue.text = quote.week52Low.format(quote.week52Low)
        } else {
            dataBinding.stats!!.stockDetail52lowValue.text = "-"
        }
        if(quote?.latestVolume != null) {
            val volume = quote.latestVolume.toDouble().div(1000000)
            dataBinding.stats!!.stockDetailVolumeValue.text = volume.format(volume) + "M"
        } else {
            dataBinding.stats!!.stockDetailVolumeValue.text = "-"
        }
        if(quote?.avgTotalVolume != null) {
            val avgVolume = quote.avgTotalVolume.toDouble().div(1000000)
            dataBinding.stats!!.stockDetailAvgVolumeValue.text = avgVolume.format(avgVolume) + "M"
        } else {
            dataBinding.stats!!.stockDetailAvgVolumeValue.text = "-"
        }
        if(quote?.marketCap != null) {
            var x = "M"
            val div: Int
            if(quote.marketCap > 1000000000) {
                div = 1000000000
                x = "B"
            } else {
                div =1000000
            }
            val cap = quote.marketCap.toDouble().div(div)
            dataBinding.stats!!.stockDetailMktcapValue.text = cap.format(cap) + x
        } else {
            dataBinding.stats!!.stockDetailMktcapValue.text = "-"
        }
        dataBinding.stats!!.stockDetailPeratioValue.text = quote?.peRatio.toString()
        if(quote?.ytdChange != null) {
            dataBinding.stats!!.stockDetailYtdValue.text = quote.ytdChange.format(quote.ytdChange)
        } else {
            dataBinding.stats!!.stockDetailYtdValue.text = "-"
        }
        dataBinding.stats!!.stockDetailDivRateValue.text = stock.stats?.dividendRate.toString()
        if(stock.stats?.dividendYield != null) {
            dataBinding.stats!!.stockDetailDivYieldValue.text = stock.stats.dividendYield.format(stock.stats.dividendYield)
        } else {
            dataBinding.stats!!.stockDetailDivYieldValue.text = "-"
        }
    }

    private fun setupAbout(stock: Stock) {
        dataBinding.about?.companyDescription?.text = stock.company?.description
        dataBinding.about?.companyCeo?.text = "CEO: " + stock.company?.cEO
        dataBinding.about?.companySector?.text = "SECTOR: " + stock.company?.sector
        dataBinding.about?.companyIndustry?.text = "INDUSTRY: " + stock.company?.industry
        dataBinding.about?.companyExchange?.text = "EXCHANGE: " + stock.company?.exchange
    }

    private fun setupFinancial(stock: Stock) {
        if(stock.financials?.financials?.isNotEmpty()!!) {
            val stat = stock.financials.financials[0]
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.CEILING
            dataBinding.financial!!.stockDetailReportDateValue.text = stat?.reportDate
            if(stat?.grossProfit != null) {
                if(stat.grossProfit > 1000000000)
                    dataBinding.financial!!.stockDetailGrossProfitValue.text = "${df.format(stat.grossProfit.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailGrossProfitValue.text = "${df.format(stat.grossProfit.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailGrossProfitValue.text = "-"
            }
            if(stat?.costOfRevenue != null) {
                if(stat.costOfRevenue > 1000000000)
                dataBinding.financial!!.stockDetailCostOfRevenueValue.text = "${df.format(stat.costOfRevenue.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailCostOfRevenueValue.text = "${df.format(stat.costOfRevenue.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailCostOfRevenueValue.text = "-"
            }
            if(stat?.operatingRevenue != null) {
                if(stat.operatingRevenue > 1000000000)
                dataBinding.financial!!.stockDetailOperatingRevenueValue.text = "${df.format(stat.operatingRevenue.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailOperatingRevenueValue.text = "${df.format(stat.operatingRevenue.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailOperatingRevenueValue.text = "-"
            }
            if(stat?.totalRevenue != null) {
                if(stat.totalRevenue > 1000000000)
                dataBinding.financial!!.stockDetailTotalRevenueValue.text = "${df.format(stat.totalRevenue.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailTotalRevenueValue.text = "${df.format(stat.totalRevenue.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailTotalRevenueValue.text = "-"
            }
            if(stat?.operatingIncome != null) {
                if(stat.operatingIncome > 1000000000)
                dataBinding.financial!!.stockDetailOperatingIncomeValue.text = "${df.format(stat.operatingIncome.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailOperatingIncomeValue.text = "${df.format(stat.operatingIncome.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailOperatingIncomeValue.text = "-"
            }
            if(stat?.netIncome != null) {
                if(stat.netIncome > 1000000000)
                dataBinding.financial!!.stockDetailNetIncomeValue.text = "${df.format(stat.netIncome.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailNetIncomeValue.text = "${df.format(stat.netIncome.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailNetIncomeValue.text = "-"
            }
            if(stat?.researchAndDevelopment != null) {
                if(stat.researchAndDevelopment > 1000000000)
                dataBinding.financial!!.stockDetailRdValue.text = "${df.format(stat.researchAndDevelopment.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailRdValue.text = "${df.format(stat.researchAndDevelopment.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailRdValue.text = "-"
            }
            if(stat?.operatingExpense != null) {
                if(stat.operatingExpense > 1000000000)
                dataBinding.financial!!.stockDetailOperatingExpenseValue.text = "${df.format(stat.operatingExpense.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailOperatingExpenseValue.text = "${df.format(stat.operatingExpense.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailOperatingExpenseValue.text = "-"
            }
            if(stat?.currentAssets != null) {
                if(stat.currentAssets > 1000000000)
                dataBinding.financial!!.stockDetailCurrentAssetsValue.text = "${df.format(stat.currentAssets.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailCurrentAssetsValue.text = "${df.format(stat.currentAssets.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailCurrentAssetsValue.text = "-"
            }
            if(stat?.totalAssets != null) {
                if(stat.totalAssets > 1000000000)
                dataBinding.financial!!.stockDetailTotaltAssetsValue.text = "${df.format(stat.totalAssets.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailTotaltAssetsValue.text = "${df.format(stat.totalAssets.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailTotaltAssetsValue.text = "-"
            }
            if(stat?.currentCash != null) {
                if(stat.currentCash > 1000000000)
                dataBinding.financial!!.stockDetailCurrentCashValue.text = "${df.format(stat.currentCash.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailCurrentCashValue.text = "${df.format(stat.currentCash.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailCurrentCashValue.text = "-"
            }
            if(stat?.totalCash != null) {
                if(stat.totalCash > 1000000000)
                dataBinding.financial!!.stockDetailTotalCashValue.text = "${df.format(stat.totalCash.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailTotalCashValue.text = "${df.format(stat.totalCash.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailTotalCashValue.text = "-"
            }
            if(stat?.cashFlow != null) {
                if(stat.cashFlow > 1000000000)
                dataBinding.financial!!.stockDetailCashFlowValue.text = "${df.format(stat.cashFlow.toDouble().div(1000000000))}B"
                else dataBinding.financial!!.stockDetailCashFlowValue.text = "${df.format(stat.cashFlow.toDouble().div(1000000))}M"
            } else {
                dataBinding.financial!!.stockDetailCashFlowValue.text = "-"
            }
        } else {
            dataBinding.financialCard.visibility = View.GONE
        }
    }

    private fun setupFab() {
        viewModel.getUserStockList().observe(this, Observer {
            if(it != null) {
                for (i in it ){
                    if(i.symbol.equals(symbol, ignoreCase = true)){
                        isFav = true
                        dataBinding.fab.setImageDrawable(this.drawable(R.drawable.ic_heart_filled))
                    }
                }
            }
            dataBinding.fab.setOnClickListener {
                if(isFav) {
                    dataBinding.fab.setImageDrawable(this.drawable(R.drawable.ic_heart_empty))
                    viewModel.deleteStock(symbol)
                } else {
                    dataBinding.fab.setImageDrawable(this.drawable(R.drawable.ic_heart_filled))
                    viewModel.insertStockToUserList(symbol, this)
                }
                isFav = !isFav
            }
        })
    }

    override fun onOffsetChanged(appBarLayout: AppBarLayout, i: Int) {
        if (mMaxScrollSize == 0)
            mMaxScrollSize = appBarLayout.totalScrollRange

        val currentScrollPercentage = Math.abs(i) * 100 / mMaxScrollSize

        if (currentScrollPercentage >= PERCENTAGE_TO_SHOW_IMAGE) {
            if (!mIsImageHidden) {
                mIsImageHidden = true

                ViewCompat.animate(dataBinding.fab).scaleY(0f).scaleX(0f).start()
                dataBinding.collapsing.title = company
            }
        }

        if (currentScrollPercentage < PERCENTAGE_TO_SHOW_IMAGE) {
            if (mIsImageHidden) {
                mIsImageHidden = false
                ViewCompat.animate(dataBinding.fab).scaleY(1f).scaleX(1f).start()
                dataBinding.collapsing.title = " "//carefull there should a space between double quote otherwise it wont work
            }
        }
    }

    private fun setupGraph(stock: Stock){
        initializeGraph()
        init1DGraph(stock)
        // set up button on click listeners
        dataBinding.graph!!.chartButton1D.setOnClickListener { init1DGraph(stock) }

        dataBinding.graph!!.chartButton1M.setOnClickListener { initGraph(stock, "1m") }

        dataBinding.graph!!.chartButton3M.setOnClickListener { initGraph(stock, "3m") }

        dataBinding.graph!!.chartButton1Y.setOnClickListener { initGraph(stock, "1y") }

        dataBinding.graph!!.chartButton5Y.setOnClickListener { initGraph(stock, "5y") }
    }

    private fun initializeGraph() {
        val mChart = dataBinding.graph!!.stockDetailGraph
        mChart.setViewPortOffsets(0f, 0f, 0f, 0f)
        mChart.setBackgroundColor(this.color(R.color.black1))

        // no description text
        mChart.description.isEnabled = false

        // enable touch gestures
        mChart.setTouchEnabled(true)

        // enable scaling and dragging
        mChart.isDragEnabled = false
        mChart.setScaleEnabled(false)

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(false)

        mChart.setDrawGridBackground(false)
        mChart.maxHighlightDistance = 300f

        val x = mChart.xAxis
        x.isEnabled = false

        val y = mChart.axisLeft
        y.isEnabled = false
        mChart.axisRight.isEnabled = false

        // add data
        // setData(25, 100f, mChart)

        mChart.legend.isEnabled = false

        mChart.animateXY(2000, 2000)

        // dont forget to refresh the drawing
        mChart.invalidate()

    }

    private fun init1DGraph(stock: Stock) {
        viewModel.getTodayChart(symbol).observe(this,
                Observer {
                    // draw graph
                    val xValues = java.util.ArrayList<String>()
                    val yValues = java.util.ArrayList<Entry>()
                    var counter = 0
                    if(it != null) {
                        var skipSteps = it.size / 30
                        if(skipSteps == 0)
                            skipSteps = 1
                        for(i in 1..it.size step skipSteps) {
                            var i = i
                            if(i >= it.size-1) {
                                i = it.size-1
                            }
                            val time = it[i].minute
                            val open = it[i].open
                            if(open != null && time != null){
                                yValues.add(Entry(counter.toFloat(), open.toFloat()))
                                xValues.add(time)
                                counter++
                            }
                            else {
                                continue
                            }
                        }
                        setGraphData(xValues, yValues, stock, null)
                    }
                })
    }

    private fun initGraph(stock: Stock, range: String) {
        viewModel.getChart(symbol, range).observe(this,
                Observer {
                    val xValues = java.util.ArrayList<String>()
                    val yValues = java.util.ArrayList<Entry>()
                    var counter = 0
                    if(it != null) {
                        var skipSteps = it.size / 30
                        if(skipSteps == 0)
                            skipSteps = 1
                        for(i in 1 until it.size step skipSteps) {
                            var i = i
                            if(i >= it.size-1) {
                                i = it.size-1
                            }
                            val date = it[i].date
                            val open = it[i].open
                            if(open != null && date != null) {
                                yValues.add(Entry(counter.toFloat(), open.toFloat()))
                                xValues.add(date.toString())
                                counter++
                            }
                            else {
                                continue
                            }
                            setGraphData(xValues, yValues, stock, it)
                        }
                    }
                })
    }

    private fun setGraphData(xValues: ArrayList<String>, yValues: ArrayList<Entry>, stock: Stock, chart: List<Chart>?) {
        val mChart = dataBinding.graph!!.stockDetailGraph
        val xAxis = mChart.xAxis
        xAxis.valueFormatter = IAxisValueFormatter { value, _ -> xValues[value.toInt() % xValues.size] }

        mChart.setOnChartValueSelectedListener(object : OnChartValueSelectedListener {
            override fun onNothingSelected() {
                dataBinding.stockDetailPrice.text = "$" + "${stock.quote?.latestPrice}"
            }

            override fun onValueSelected(e: Entry?, h: Highlight?) {
                dataBinding.stockDetailPrice.text = "$" + e?.y?.toDouble()?.format(e.y.toDouble())
            }

        })

        val set1 = LineDataSet(yValues, "DataSet 1")
        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.cubicIntensity = 0.2f
        //set1.setDrawFilled(true);
        set1.setDrawCircles(false)
        set1.lineWidth = 1.8f
        set1.circleRadius = 4f
        set1.setCircleColor(Color.WHITE)
        if(chart == null) {
            val percentage = stock.quote?.changePercent
            if(percentage != null) {
                // dataBinding.stockDetailPercentage.text = "${percentage.format(percentage)}%"
                dataBinding.stockDetailPercentage.text = "$" + stock.quote.change?.format(stock.quote.change) + " (" + percentage.format(percentage) + "%)"
                if(percentage > 0) {
                    set1.highLightColor = this.color(R.color.green)
                    set1.color = this.color(R.color.green)
                    dataBinding.stockDetailPercentage.setTextColor(this.color(R.color.green))
                } else {
                    set1.highLightColor = this.color(R.color.red)
                    set1.color = this.color(R.color.red)
                    dataBinding.stockDetailPercentage.setTextColor(this.color(R.color.red))
                }
            }
        } else {
            val open = chart[0].close
            val close = chart[chart.size - 1].close
            if(open != null && close != null) {
                val difference = close - open
                val percentChange = difference / open * 100
                if(open < close) {
                    set1.highLightColor = this.color(R.color.green)
                    set1.color = this.color(R.color.green)
                    dataBinding.stockDetailPercentage.text = "$${difference.format(difference)} (${percentChange.format(percentChange)}%)"
                    dataBinding.stockDetailPercentage.setTextColor(this.color(R.color.green))
                } else {
                    set1.highLightColor = this.color(R.color.red)
                    set1.color = this.color(R.color.red)
                    dataBinding.stockDetailPercentage.text = "$${difference.format(difference)} (${percentChange.format(percentChange)}%)"
                    dataBinding.stockDetailPercentage.setTextColor(this.color(R.color.red))
                }
            }
        }

        set1.setDrawHorizontalHighlightIndicator(false)
        val dataSets = java.util.ArrayList<ILineDataSet>()
        dataSets.add(set1)
        val data = LineData(dataSets)
        data.setDrawValues(false)
        mChart.data = data
        mChart.animateXY(2000, 2000)
    }

    private fun setupEarnings(stock: Stock) {
        val mChart = dataBinding.earning!!.stockDetailGraph
        val earnings = stock.earnings?.earnings ?: return

        mChart.setViewPortOffsets(0f, 0f, 0f, 0f)
        mChart.setBackgroundColor(this.color(R.color.black1))
        mChart.setNoDataText("Fetching latest graph data")
        mChart.description.isEnabled = false
        val e1 = java.util.ArrayList<BubbleEntry>()
        val e2 = java.util.ArrayList<BubbleEntry>()
        var counter = 0

        val bubbleSize = 0.3f
        for(i in earnings.size-1 downTo 0) {
            val actual = earnings[i]?.actualEPS
            val estimated = earnings[i]?.estimatedEPS
//            val date = earnings[i]?.ePSReportDate
//            val fromUser = SimpleDateFormat("yyyy-MM-dd", Locale.US)
//            val myFormat = SimpleDateFormat("yyyyMMdd", Locale.US)
//
//            var formattedDate: String = ""
//            try {
//                formattedDate = myFormat.format(fromUser.parse(date))
//            } catch (e: ParseException) {
//                e.printStackTrace()
//            }
            if(actual == null || estimated == null) continue
            e1.add(BubbleEntry(counter.toFloat(),actual.toFloat(), bubbleSize))
            e2.add(BubbleEntry(counter.toFloat(),estimated.toFloat(), bubbleSize))
            counter++
        }

        val d1 = BubbleDataSet(e1, "Actual EPS")
        d1.setDrawIcons(false)
        d1.color = this.color(R.color.grey1)
        d1.setDrawValues(false)
        d1.isNormalizeSizeEnabled = false

        val d2 = BubbleDataSet(e2, "Estimated EPS")
        d2.setDrawIcons(false)
        d2.iconsOffset = MPPointF(0f, 10f)
        d2.setColor(this.color(R.color.grey2), 150)
        d2.setDrawValues(false)
        d2.isNormalizeSizeEnabled = false

        // enable touch gestures
        mChart.setTouchEnabled(false)

        val x = mChart.xAxis
        x.position = XAxis.XAxisPosition.BOTTOM
        x.setDrawGridLines(false)
        x.axisMinimum = -1f
        x.axisMaximum = 4f

        val y = mChart.axisLeft
        y.setDrawAxisLine(false)
        y.setDrawZeroLine(false)
        y.setDrawGridLines(false)
        mChart.axisRight.setDrawGridLines(false)

        val dataSets = java.util.ArrayList<IBubbleDataSet>()
        dataSets.add(d1) // add the data sets
        dataSets.add(d2)

        val cd = BubbleData(dataSets)
        mChart.data = cd
        mChart.animateXY(2000, 2000)
    }
}
