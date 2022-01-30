package com.example.case_construction.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.case_construction.R
import com.example.case_construction.utility.toast
import kotlinx.android.synthetic.main.dialog_enter_machine_no_manually.*

@SuppressLint("SetTextI18n")
class EnterMachineNoManuallyDialog(
    val activity: Activity,
    private val listener: DialogListener,
) : BaseDialog(activity, R.style.MyThemeDialogSelect), View.OnClickListener {
    interface DialogListener {
        fun onDoneClick(machineNo: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_enter_machine_no_manually)
        initializeDialog()
        tvDone.setOnClickListener(this)
    }


    override fun onClick(v: View?) {
        try {
            if (v != null) clickPause(v, this) else return
            when (v) {
                tvDone -> {
                    if (!validation())
                        activity.toast("Fill all the required data")
                    else {
                        listener.onDoneClick(edMachineNo.text.toString())
                        this.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun validation(): Boolean {
        if (edMachineNo.text.toString().isBlank()) {
            edMachineNo.error = "Required"
            return false
        }
        return true
    }
}