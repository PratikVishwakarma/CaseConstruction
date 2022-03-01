package com.example.case_construction.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.case_construction.R
import com.example.case_construction.model.UtilityDTO
import com.example.case_construction.network.api_model.Remark
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.utility.Constants
import com.example.case_construction.utility.getDummyUtilData
import com.example.case_construction.utility.pauseClick
import com.example.case_construction.utility.toast
import kotlinx.android.synthetic.main.dialog_add_remark.*
import kotlinx.android.synthetic.main.item_remark.*

@SuppressLint("SetTextI18n")
class AddUpdateRemarkDialog(
    val activity: Activity,
    private val listener: DialogListener,
) : BaseDialog(activity, R.style.MyThemeDialogSelect), View.OnClickListener {

    private var remarkDescription = ""
    private var remarkType = ""
    private var status = Constants.CONST_NOT_OK
    private val remark = Remark()

    interface DialogListener {
        fun onUpdateClick(
            remark: Remark,
            type: String
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_add_remark)
        initializeDialog()
        tvUpdate.setOnClickListener(this)

        edRemarkDescription.setText("")
        tvRemarkType.text = ""
        tvRemarkType.setOnClickListener {
            it.pauseClick()
            SingleItemSelectDialog(
                activity,
                "Choose City",
                object : SingleItemSelectDialog.DialogListener {
                    override fun onConfirmClick(utilityDTO: UtilityDTO) {
                        tvRemarkType.text = utilityDTO.value
                        remarkType = utilityDTO.subValue
                    }
                },
                getDummyUtilData()
            ).show()
        }
        swIsOkay.setOnCheckedChangeListener { _, b ->
            status =  if(b) Constants.CONST_OK
            else Constants.CONST_NOT_OK

            swIsOkay.text = status
        }
    }

//
//    private fun getCityList(): ArrayList<UtilityDTO> {
//        val cityList = ArrayList<UtilityDTO>()
//        (activity as MainActivity).defaultPreference.baseData.city.forEach {
//            cityList.add(UtilityDTO(false, it.cityName, it.id))
//        }
//        return cityList
//    }


    override fun onClick(v: View?) {
        try {
            if (v != null) clickPause(v, this) else return
            when (v) {
                tvUpdate -> {
                    if (!validation())
                        activity.toast("Fill all the required data")
                    else {
                        tvUpdate.pauseClick()
                        remark.description = edRemarkDescription.text.toString().trim()
                        remark.type = remarkType
                        remark.status = swIsOkay.text.toString().trim()
                        listener.onUpdateClick(
                            remark,
                            ""
                        )
                        this.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun validation(): Boolean {
        if (edRemarkDescription.text.toString().isBlank()) {
            edRemarkDescription.error = "Required"
            return false
        }
        if (remarkType.isBlank()) {
            activity.toast("Select Remark Type")
            return false
        }

        return true
    }
}