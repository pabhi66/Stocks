package com.ap.mobile.stocks.util

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager

/**
 * Activity util class
 */
object ActivityUtil {
    /**
     * adds fragment to activity
     */
    fun addFragmentToActivity(manager: FragmentManager, fragment: Fragment, frameId: Int) {
        val transaction = manager.beginTransaction()
        transaction.add(frameId, fragment)
        transaction.addToBackStack(fragment.tag)
        transaction.commit()
    }
}