package com.ap.mobile.stocks.ui.base

import android.annotation.SuppressLint
import android.app.Dialog
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.databinding.DataBindingUtil
import android.databinding.ViewDataBinding
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.annotation.LayoutRes
import android.support.annotation.NonNull
import android.support.design.widget.BottomSheetDialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.CoordinatorLayout
import com.ap.mobile.stocks.R
import kotlin.math.max


/**
 * Base fragment will extend all the fragments dialogs
 */
abstract class BaseBottomSheetDialogFragment<VM: ViewModel, DB: ViewDataBinding>: BottomSheetDialogFragment() {

    // inject view model
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    // set up data binding
    lateinit var dataBinding: DB

    // set up view model
    lateinit var viewModel: VM

    /**
     * get view model
     */
    abstract fun getViewModel(): Class<VM>

    // get layout res
    @LayoutRes
    abstract fun getLayoutRes(): Int

    // on on create setup view model and inject this fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidSupportInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(getViewModel())
    }

    // set up create view with data binding
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dataBinding = DataBindingUtil.inflate(
            inflater,
            getLayoutRes(),
            container,
            false)

//        activity?.requestWindowFeature(Window.FEATURE_NO_TITLE)
//        activity?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dataBinding.root
    }

//    @SuppressLint("RestrictedApi")
//    override fun setupDialog(dialog: Dialog, style: Int) {
//        super.setupDialog(dialog, style)
//        val contentView = View.inflate(context, R.layout.dialog_stock_detail, null)
//        dialog.setContentView(contentView)
//
//        val displayMetrics = activity?.resources?.displayMetrics
//        val width = displayMetrics?.widthPixels
//        val height = displayMetrics?.heightPixels
//
//        val maxHeight = (height?.times(0.88))?.toInt()
//
//        val params = (contentView.parent as View).layoutParams as CoordinatorLayout.LayoutParams
//        val behavior = params.behavior
//        if (behavior != null && behavior is BottomSheetBehavior<*>) {
//            behavior.setBottomSheetCallback(bottomSheetCallback)
//            behavior.peekHeight = maxHeight!!
//        }
//    }

    private val bottomSheetCallback = object : BottomSheetBehavior.BottomSheetCallback() {
        override fun onStateChanged(@NonNull bottomSheet: View, newState: Int) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss()
            }
        }

        override fun onSlide(@NonNull bottomSheet: View, slideOffset: Float) {}
    }

}