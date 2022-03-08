package com.example.case_construction.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.case_construction.R
import com.example.case_construction.model.diffs.DiffUtilMachine
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.utility.AppOnClick
import kotlinx.android.synthetic.main.item_remark.view.*

@SuppressLint("SetTextI18n")
class MachineAdapter :
    ListAdapter<Machine, MachineAdapter.MyViewHolder>(DiffUtilMachine()) {
    var appOnClick: AppOnClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_remark, parent, false)
        return MyViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(record: Machine, position: Int) {
            itemView.tvDescription.text = record.machineNo
            itemView.tvType.text = record.oKOLStatus
            itemView.tvStatus.text = record.oKOLDate
            appOnClick?.let { _ ->
                itemView.setOnClickListener {
                    appOnClick?.onClickListener(record, position)
                }
            }
        }
    }

}