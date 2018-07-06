package com.ap.mobile.stocks.ui.views.searchview.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Build
import android.support.annotation.RequiresApi
import android.support.annotation.StringRes
import android.support.transition.Transition
import android.view.View
import android.view.ViewAnimationUtils
import android.view.ViewPropertyAnimator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import com.ap.mobile.stocks.util.buildIsLollipopAndUp
import com.ap.mobile.stocks.util.invisible
import com.ap.mobile.stocks.util.visible

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/17/18.
 */

@SuppressLint("NewApi")
fun View.circularHide(x: Int = 0, y: Int = 0, offset: Long = 0L, radius: Float = -1.0f, duration: Long = 500L, onStart: (() -> Unit)? = null, onFinish: (() -> Unit)? = null) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        invisible()
        onFinish?.invoke()
        return
    }
    if (!buildIsLollipopAndUp) return fadeOut(offset, duration, onStart, onFinish)

    val r = if (radius >= 0) radius
    else Math.max(Math.hypot(x.toDouble(), y.toDouble()), Math.hypot((width - x.toDouble()), (height - y.toDouble()))).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, r, 0f).setDuration(duration)
    anim.startDelay = offset
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) = onStart?.invoke() ?: Unit

        override fun onAnimationEnd(animation: Animator?) {
            invisible()
            onFinish?.invoke() ?: Unit
        }

        override fun onAnimationCancel(animation: Animator?) = onFinish?.invoke() ?: Unit
    })
    anim.start()
}

@SuppressLint("NewApi")
fun View.circularReveal(x: Int = 0, y: Int = 0, offset: Long = 0L, radius: Float = -1.0f, duration: Long = 500L, onStart: (() -> Unit)? = null, onFinish: (() -> Unit)? = null) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        visible()
        onFinish?.invoke()
        return
    }
    if (!buildIsLollipopAndUp) return fadeIn(offset, duration, onStart, onFinish)

    val r = if (radius >= 0) radius
    else Math.max(Math.hypot(x.toDouble(), y.toDouble()), Math.hypot((width - x.toDouble()), (height - y.toDouble()))).toFloat()

    val anim = ViewAnimationUtils.createCircularReveal(this, x, y, 0f, r).setDuration(duration)
    anim.startDelay = offset
    anim.addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator?) {
            visible()
            onStart?.invoke()
        }

        override fun onAnimationEnd(animation: Animator?) = onFinish?.invoke() ?: Unit
        override fun onAnimationCancel(animation: Animator?) = onFinish?.invoke() ?: Unit
    })
    anim.start()
}

fun View.fadeOut(offset: Long = 0L, duration: Long = 200L, onStart: (() -> Unit)? = null, onFinish: (() -> Unit)? = null) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        invisible()
        onFinish?.invoke()
        return
    }
    val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_out)
    anim.startOffset = offset
    anim.duration = duration
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) {
            invisible()
            onFinish?.invoke()
        }

        override fun onAnimationStart(animation: Animation?) {
            onStart?.invoke()
        }
    })
    startAnimation(anim)
}

fun View.fadeIn(offset: Long = 0L, duration: Long = 200L, onStart: (() -> Unit)? = null, onFinish: (() -> Unit)? = null) {
    if (!isAttachedToWindow) {
        onStart?.invoke()
        visible()
        onFinish?.invoke()
        return
    }
    val anim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in)
    anim.startOffset = offset
    anim.duration = duration
    anim.setAnimationListener(object : Animation.AnimationListener {
        override fun onAnimationRepeat(animation: Animation?) {}
        override fun onAnimationEnd(animation: Animation?) = onFinish?.invoke() ?: Unit
        override fun onAnimationStart(animation: Animation?) {
            visible()
            onStart?.invoke()
        }
    })
    startAnimation(anim)
}

fun TextView.setTextWithFade(text: String, duration: Long = 200, onFinish: (() -> Unit)? = null) {
    fadeOut(duration = duration, onFinish = {
        setText(text)
        fadeIn(duration = duration, onFinish = onFinish)
    })
}

fun TextView.setTextWithFade(@StringRes textId: Int, duration: Long = 200, onFinish: (() -> Unit)? = null) = setTextWithFade(context.getString(textId), duration, onFinish)

fun ViewPropertyAnimator.scaleXY(value: Float) = scaleX(value).scaleY(value)

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun Transition.addEndListener(onEnd: (transition: Transition) -> Unit) {
    addListener(SupportTransitionEndListener(onEnd))
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
class SupportTransitionEndListener(val onEnd: (transition: Transition) -> Unit) : Transition.TransitionListener {
    override fun onTransitionEnd(transition: Transition) = onEnd(transition)
    override fun onTransitionResume(transition: Transition) {}
    override fun onTransitionPause(transition: Transition) {}
    override fun onTransitionCancel(transition: Transition) {}
    override fun onTransitionStart(transition: Transition) {}
}