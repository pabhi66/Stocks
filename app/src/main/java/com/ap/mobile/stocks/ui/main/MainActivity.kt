package com.ap.mobile.stocks.ui.main

import android.app.AlertDialog
import android.os.Bundle
import com.ap.mobile.stocks.R
import com.ap.mobile.stocks.databinding.ActivityMainBinding
import com.ap.mobile.stocks.ui.base.BaseActivity
import com.ap.mobile.stocks.util.NetworkUtil

/**
 * Main class which starts the main fragment
 */
class MainActivity : BaseActivity<ActivityMainBinding>() {
    override fun getLayoutRes(): Int = R.layout.activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSupportActionBar(dataBinding.toolbar)

        // check if there is internet access
        if(NetworkUtil.getConnectionStatus(this) == 0) {
            showErrorDialog("You are not connected to the Internet. None of the functionality will work.")
        }
    }

    override fun getMenuRes(): Int = R.menu.menu_main

    /**
     * show error
     */
    private fun showErrorDialog(message: String) {
        val builder1 = AlertDialog.Builder(this)
        builder1.setMessage(message)
        builder1.setCancelable(false)

        builder1.setPositiveButton(
            "ok",
            { dialog, _ -> dialog.cancel() })

        val alert11 = builder1.create()
        alert11.show()
    }
}
