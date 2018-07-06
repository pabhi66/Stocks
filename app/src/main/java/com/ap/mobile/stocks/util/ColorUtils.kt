package com.ap.mobile.stocks.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import android.support.annotation.AttrRes
import android.support.annotation.ColorInt
import android.support.v7.widget.AppCompatEditText
import android.widget.*
import com.ap.mobile.stocks.R
import com.mikepenz.iconics.IconicsDrawable
import com.mikepenz.iconics.typeface.IIcon

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/17/18.
 */
fun IIcon.toDrawable(c: Context, sizeDp: Int = 24, @ColorInt color: Int = Color.WHITE, builder: IconicsDrawable.() -> Unit = {}): Drawable {
    val state = ColorStateList.valueOf(color)
    val icon = IconicsDrawable(c).icon(this).sizeDp(sizeDp).color(state)
    icon.builder()
    return icon
}

fun createSimpleRippleDrawable(@ColorInt foregroundColor: Int, @ColorInt backgroundColor: Int): RippleDrawable {
    val states = ColorStateList(arrayOf(intArrayOf()), intArrayOf(foregroundColor))
    val content = ColorDrawable(backgroundColor)
    val mask = ColorDrawable(foregroundColor.adjustAlpha(0.16f))
    return RippleDrawable(states, content, mask)
}

@ColorInt
fun Int.adjustAlpha(factor: Float): Int {
    val alpha = Math.round(Color.alpha(this) * factor)
    return Color.argb(alpha, Color.red(this), Color.green(this), Color.blue(this))
}

@SuppressLint("RestrictedApi")
fun EditText.tint(@ColorInt color: Int) {
    val editTextColorStateList = context.textColorStateList(color)
    if (this is AppCompatEditText) {
        supportBackgroundTintList = editTextColorStateList
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        backgroundTintList = editTextColorStateList
    }
    tintCursor(color)
}

fun Context.textColorStateList(@ColorInt color: Int): ColorStateList {
    val states = arrayOf(
        intArrayOf(-android.R.attr.state_enabled),
        intArrayOf(-android.R.attr.state_pressed, -android.R.attr.state_focused),
        intArrayOf()
    )
    val colors = intArrayOf(
        resolveColor(R.attr.colorControlNormal),
        resolveColor(R.attr.colorControlNormal),
        color
    )
    return ColorStateList(states, colors)
}

//Attr retrievers
fun Context.resolveColor(@AttrRes attr: Int, fallback: Int = 0): Int {
    val a = theme.obtainStyledAttributes(intArrayOf(attr))
    try {
        return a.getColor(0, fallback)
    } finally {
        a.recycle()
    }
}

fun EditText.tintCursor(@ColorInt color: Int) {
    try {
        val fCursorDrawableRes = TextView::class.java.getDeclaredField("mCursorDrawableRes")
        fCursorDrawableRes.isAccessible = true
        val mCursorDrawableRes = fCursorDrawableRes.getInt(this)
        val fEditor = TextView::class.java.getDeclaredField("mEditor")
        fEditor.isAccessible = true
        val editor = fEditor.get(this)
        val clazz = editor.javaClass
        val fCursorDrawable = clazz.getDeclaredField("mCursorDrawable")
        fCursorDrawable.isAccessible = true
        val drawables: Array<Drawable> = Array(2) {
            val drawable = context.drawable(mCursorDrawableRes)
            drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN)
            drawable
        }
        fCursorDrawable.set(editor, drawables)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}
