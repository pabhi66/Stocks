package com.ap.mobile.stocks.util

import android.support.v4.widget.ViewDragHelper

/**
 * @author Abhishek Prajapati
 * @version 1.0.0
 * @since 1/17/18.
 */
const val LEFT = ViewDragHelper.EDGE_LEFT
const val RIGHT = ViewDragHelper.EDGE_RIGHT
const val TOP = ViewDragHelper.EDGE_TOP
const val BOTTOM = ViewDragHelper.EDGE_BOTTOM
const val HORIZONTAL = LEFT or RIGHT
const val VERTICAL = TOP or BOTTOM
const val ALL = HORIZONTAL or VERTICAL

const val COLLAPSED = 0
const val COLLAPSING = 1
const val EXPANDING = 2
const val EXPANDED = 3

const val ELLIPSIS = '\u2026'