package com.example.case_construction.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.case_construction.R
import com.example.case_construction.adapter.SelectionAdapter
import com.example.case_construction.model.UtilityDTO
import com.example.case_construction.utility.AppOnClick
import kotlinx.android.synthetic.main.dialog_single_item_select.*

@SuppressLint("SetTextI18n")
class SingleItemSelectDialog(
    val activity: Activity,
    private val dialogTitle: String,
    private val listener: DialogListener,
    private val list: ArrayList<UtilityDTO>
) : BaseDialog(activity,R.style.MyThemeDialogSelect), View.OnClickListener {

    private var selectedItem: UtilityDTO? = null

    interface DialogListener {
        fun onConfirmClick(utilityDTO: UtilityDTO)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_single_item_select)
        initializeDialog()
        tvCancel.setOnClickListener(this)
        tvCancel.visibility = View.GONE
        tvDialogTitle.text = dialogTitle
        val mAdapter = SelectionAdapter()

        rvList.adapter = mAdapter
        mAdapter.submitList(list)

        mAdapter.appOnClick = object : AppOnClick {
            override fun onClickListener(item: Any, position: Int, view: View?) {
                listener.onConfirmClick(item as UtilityDTO)
                dismiss()
            }
        }
    }


    override fun onClick(v: View?) {
        try {
            if (v != null) clickPause(v, this) else return
            when (v) {
                tvCancel -> this.dismiss()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}