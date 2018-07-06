package com.ap.mobile.stocks.util

import android.os.Build

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/17/18.
 */
inline val buildIsMarshmallowAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

inline val buildIsLollipopAndUp: Boolean
    get() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP