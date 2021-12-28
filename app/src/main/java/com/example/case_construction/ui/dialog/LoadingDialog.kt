package com.example.case_construction.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.Window
import com.example.case_construction.R
import kotlinx.android.synthetic.main.dialog_loading.*

@SuppressLint("SetTextI18n")
class LoadingDialog(
    val activity: Activity,
    private val dialogTitle: String = "Loading..."
) : BaseDialog(activity) {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_loading)
        initializeDialog()
        this.setCancelable(false)
        this.setCanceledOnTouchOutside(false)
        tvLoadingMessage.text = dialogTitle
    }


}