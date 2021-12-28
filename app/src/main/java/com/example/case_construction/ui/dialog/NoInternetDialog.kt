package com.example.case_construction.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.Window
import com.example.case_construction.R
import kotlinx.android.synthetic.main.dialog_error.*

@SuppressLint("SetTextI18n")
class NoInternetDialog(
    val activity: Activity,
    val listener: DialogListener
) : BaseDialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_error)
        initializeDialog()
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
        tvOk.setOnClickListener {
            listener.onOkClick()
            dismiss()
        }
    }

    interface DialogListener {
        fun onOkClick()
    }
}