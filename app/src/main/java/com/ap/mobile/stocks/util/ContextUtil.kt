@file:Suppress("NOTHING_TO_INLINE")

package com.ap.mobile.stocks.util

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.annotation.FontRes
import android.support.annotation.StringRes
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import java.text.DecimalFormat

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/17/18.
 */

inline fun Context.color(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

@SuppressLint("ResourceType")
inline fun Context.string(@StringRes id: Int, fallback: String?): String? = if (id > 0) string(id) else fallback

inline fun Context.string(@StringRes id: Int): String = getString(id)

inline fun Context.drawable(@DrawableRes id: Int): Drawable = ContextCompat.getDrawable(this, id) ?: throw RuntimeException("Drawable with id $id not found")

inline fun Context.font(@FontRes id: Int): Typeface = ResourcesCompat.getFont(this, id) ?: throw RuntimeException("Drawable with id $id not found")

inline fun Double.format(number: Double): String {
    val df = DecimalFormat("#.##")
    return df.format(number)
}