package com.example.case_construction.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.case_construction.R
import kotlinx.android.synthetic.main.dialog_error.*

@SuppressLint("SetTextI18n")
class ErrorDialog(
    val activity: Activity,
    val listener: DialogListener,
    val message: String,
) : BaseDialog(activity) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_error)
        initializeDialog()
        tvMessage.text = message
        lottie.visibility = View.GONE
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