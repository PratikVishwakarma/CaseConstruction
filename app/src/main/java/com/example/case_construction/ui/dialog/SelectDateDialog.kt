package com.example.case_construction.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.case_construction.R
import com.example.case_construction.utility.toast
import kotlinx.android.synthetic.main.dialog_select_date.*
import java.util.*

@SuppressLint("SetTextI18n")
class SelectDateDialog(
    val activity: Activity,
    private val listener: DialogListener,
) : BaseDialog(activity, R.style.MyThemeDialogSelect), View.OnClickListener {
    var selectedDate = ""
    interface DialogListener {
        fun onDoneClick(date: String)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_select_date)
        initializeDialog()
        val instance = Calendar.getInstance()
        datePicker.maxDate = instance.timeInMillis
        instance.set(Calendar.YEAR, 2021)
        datePicker.minDate = instance.timeInMillis
        tvDone.setOnClickListener(this)
        datePicker.init(
            Calendar.getInstance().get(Calendar.YEAR),
            Calendar.getInstance().get(Calendar.MONTH),
            Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        ) { p0, year, month, day -> selectedDate = "$year/$month/$day" }
    }


    override fun onClick(v: View?) {
        try {
            if (v != null) clickPause(v, this) else return
            when (v) {
                tvDone -> {
                    if (!validation())
                        activity.toast("Fill all the required data")
                    else {
                        listener.onDoneClick(selectedDate)
                        this.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun validation(): Boolean {
        if (selectedDate.isBlank()) {
            "Please select a date".toast(activity)
            return false
        }
        return true
    }
}