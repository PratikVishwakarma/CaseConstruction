package com.example.case_construction.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.case_construction.R
import com.example.case_construction.model.diffs.DiffUtilMachine
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.utility.AppOnClick
import com.example.case_construction.utility.Constants
import com.example.case_construction.utility.PreferenceHelper.currentUser
import com.example.case_construction.utility.printLog
import kotlinx.android.synthetic.main.item_machine.view.*

@SuppressLint("SetTextI18n")
class MachineAdapter(val context: Context, val userType: String) :
    ListAdapter<Machine, MachineAdapter.MyViewHolder>(DiffUtilMachine()) {
    var appOnClick: AppOnClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_machine, parent, false)
        return MyViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(machine: Machine, position: Int) {
            itemView.tvMachineNo.text = machine.machineNo
            var userTypePDI = userType
            if (userType == Constants.CONST_USERTYPE_PDI_EXPORT || userType == Constants.CONST_USERTYPE_PDI_DOMESTIC)
                userTypePDI = "PDI"
            var value1 = ""
            var value2 = ""
            when (userType) {
                Constants.CONST_USERTYPE_OKOL -> {
                    value1 = machine.oKOLStatus
                    value2 = machine.oKOLDate
                }
                Constants.CONST_USERTYPE_TESTING -> {
                    value1 = machine.testingStatus
                    value2 = machine.testingDate
                }
                Constants.CONST_USERTYPE_FINISHING -> {
                    value1 = machine.finishStatus
                    value2 = machine.finishDate
                }
                Constants.CONST_USERTYPE_PDI_DOMESTIC, Constants.CONST_USERTYPE_PDI_EXPORT -> {
                    value1 = machine.pdiStatus
                    value2 = machine.pdiDate
                }
            }
            itemView.tvStage.text = value1
            itemView.tvDate.text = value2
            val filter = machine.rework.filter {
                it.reworkFrom == userTypePDI
            }
            "filtered list size: ${filter.size}".printLog(javaClass.name)
            "filtered list size usertype: $value2".printLog(javaClass.name)
            "filtered list size usertype: $userType".printLog(javaClass.name)
            if(filter.isEmpty()){
                itemView.tvNoRecord.visibility = View.VISIBLE
                itemView.rvListRework.visibility = View.GONE
            }
            else {
                itemView.tvNoRecord.visibility = View.GONE
                itemView.rvListRework.visibility = View.VISIBLE
                val mAdapter = RemarkAdapter(context)
                mAdapter.submitList(filter)
                itemView.rvListRework.adapter = mAdapter
            }
        }
    }

}