package com.ap.mobile.stocks.ui.views.toolbarindicator

import android.animation.Animator
import android.animation.AnimatorInflater
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.annotation.DrawableRes
import android.support.v4.view.ViewPager
import android.text.TextPaint
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.animation.Interpolator
import android.widget.LinearLayout
import com.ap.mobile.stocks.R
import android.support.v4.view.ViewPager.OnPageChangeListener
import com.ap.mobile.stocks.util.DimensionUtil


class ToolbarIndicator : LinearLayout, OnPageChangeListener {
    private var mViewpager: ViewPager? = null
    private var mIndicatorMargin: Int = 0
    private var mIndicatorWidth: Int = 0
    private var mIndicatorHeight: Int = 0
    private var mAnimatorResId = R.animator.scale_with_alpha
    private var mAnimatorReverseResId = -1
    private var mIndicatorBackground = R.drawable.white_radius
    private var mIndicatorUnselectedBackground = R.drawable.white_radius
    private var mCurrentPosition = 0
    private var mAnimationOut: Animator? = null
    private var mAnimationIn: Animator? = null
    private var title: Array<String>? = null
    private var textPaint: TextPaint? = null
    private var scrollXx: Int = 0

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER
        handleTypedArray(context, attrs)
        mAnimationOut = AnimatorInflater.loadAnimator(context, mAnimatorResId)
        if (mAnimatorReverseResId == -1) {
            mAnimationIn = AnimatorInflater.loadAnimator(context, mAnimatorResId)
            mAnimationIn!!.interpolator = ReverseInterpolator()
        } else {
            mAnimationIn = AnimatorInflater.loadAnimator(context, mAnimatorReverseResId)
        }

        setPadding(0, DimensionUtil.dpToPx((TEXT_SIZE + 8).toFloat()), 0, 0)
        setBackgroundColor(0x00000000)

        textPaint = TextPaint()
        textPaint!!.color = Color.WHITE
        textPaint!!.isAntiAlias = true
        textPaint!!.textSize = DimensionUtil.dpToPx(TEXT_SIZE.toFloat()).toFloat()
        textPaint!!.textAlign = Paint.Align.CENTER
    }

    private fun handleTypedArray(context: Context, attrs: AttributeSet?) {
        if (attrs != null) {
            val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ToolbarIndicator)
            mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.ToolbarIndicator_ti_width, -1)
            mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.ToolbarIndicator_ti_height, -1)
            mIndicatorMargin = typedArray.getDimensionPixelSize(R.styleable.ToolbarIndicator_ti_margin, -1)

            mAnimatorResId = typedArray.getResourceId(R.styleable.ToolbarIndicator_ti_animator,
                    R.animator.scale_with_alpha)
            mAnimatorReverseResId = typedArray.getResourceId(R.styleable.ToolbarIndicator_ti_animator_reverse, -1)
            mIndicatorBackground = typedArray.getResourceId(R.styleable.ToolbarIndicator_ti_drawable,
                    R.drawable.white_radius)
            mIndicatorUnselectedBackground = typedArray.getResourceId(R.styleable.ToolbarIndicator_ti_drawable_unselected,
                    mIndicatorBackground)
            typedArray.recycle()
        }
        mIndicatorWidth = if (mIndicatorWidth == -1) DimensionUtil.dpToPx(DEFAULT_INDICATOR_WIDTH.toFloat()) else mIndicatorWidth
        mIndicatorHeight = if (mIndicatorHeight == -1) DimensionUtil.dpToPx(DEFAULT_INDICATOR_WIDTH.toFloat()) else mIndicatorHeight
        mIndicatorMargin = if (mIndicatorMargin == -1) DimensionUtil.dpToPx(DEFAULT_INDICATOR_WIDTH.toFloat()) else mIndicatorMargin
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = width
        val center = width / 2
        var x: Int
        var y: Int
        var alpha: Int
        val alphaOffsetXMax = (center * 1.0f).toInt()

        val ratio = scrollXx * 1.0f / mViewpager!!.width

        for (i in title!!.indices) {
            x = (i * width - ratio * width + center).toInt()
            y = (height * 0.5f + DimensionUtil.dpToPx((TEXT_SIZE + 8).toFloat()) * 0.25f).toInt()
            val alphaOffsetX = Math.abs(x - center)

            alpha = if (alphaOffsetX > alphaOffsetXMax) {
                0
            } else {
                ((1.0f - alphaOffsetX * 1.0f / alphaOffsetXMax) * 255).toInt()
            }
            textPaint!!.alpha = alpha
            canvas.drawText(title!![i], x.toFloat(), y.toFloat(), textPaint!!)
        }
    }

    fun setViewPager(viewPager: ViewPager) {
        mViewpager = viewPager
        mCurrentPosition = mViewpager!!.currentItem
        createIndicators(viewPager)
        mViewpager!!.addOnPageChangeListener(this)
        onPageSelected(mCurrentPosition)

        this.title = Array(viewPager.adapter!!.count){"it = $it"}
        for (i in title!!.indices) {
            title!![i] = viewPager.adapter!!.getPageTitle(i).toString()
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float,
                                positionOffsetPixels: Int) {
        scrollXx = position * mViewpager!!.width + positionOffsetPixels
        invalidate()
    }

    override fun onPageSelected(position: Int) {
        if (mAnimationIn!!.isRunning) mAnimationIn!!.cancel()
        if (mAnimationOut!!.isRunning) mAnimationOut!!.cancel()

        val currentIndicator = getChildAt(mCurrentPosition)
        currentIndicator.setBackgroundResource(mIndicatorUnselectedBackground)
        mAnimationIn!!.setTarget(currentIndicator)
        mAnimationIn!!.start()

        val selectedIndicator = getChildAt(position)
        selectedIndicator.setBackgroundResource(mIndicatorBackground)
        mAnimationOut!!.setTarget(selectedIndicator)
        mAnimationOut!!.start()

        mCurrentPosition = position
    }

    override fun onPageScrollStateChanged(state: Int) {}

    private fun createIndicators(viewPager: ViewPager) {
        removeAllViews()
        val count = viewPager.adapter!!.count
        if (count <= 0) {
            return
        }

        addIndicator(mIndicatorBackground)

        for (i in 1 until count) {
            addIndicator(mIndicatorUnselectedBackground)
        }

        mAnimationOut!!.setTarget(getChildAt(mCurrentPosition))
        mAnimationOut!!.start()
    }

    private fun addIndicator(@DrawableRes backgroundDrawableId: Int) {
        val indicator = View(context)
        indicator.setBackgroundResource(backgroundDrawableId)
        addView(indicator, mIndicatorWidth, mIndicatorHeight)
        val lp = indicator.layoutParams as LinearLayout.LayoutParams
        lp.leftMargin = mIndicatorMargin
        lp.rightMargin = mIndicatorMargin
        indicator.layoutParams = lp
        mAnimationOut!!.setTarget(indicator)
        mAnimationOut!!.start()
    }

    private inner class ReverseInterpolator : Interpolator {
        override fun getInterpolation(value: Float): Float {
            return Math.abs(1.0f - value)
        }
    }

    companion object {
        private const val DEFAULT_INDICATOR_WIDTH = 3
        private const val TEXT_SIZE = 20
    }


}
