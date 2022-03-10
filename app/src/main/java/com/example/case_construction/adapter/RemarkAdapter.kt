package com.example.case_construction.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.case_construction.R
import com.example.case_construction.model.diffs.DiffUtilRemarkDTO
import com.example.case_construction.network.api_model.Rework
import com.example.case_construction.utility.AppOnClick
import com.example.case_construction.utility.Constants
import com.example.case_construction.utility.printLog
import kotlinx.android.synthetic.main.item_remark.view.*

@SuppressLint("SetTextI18n")
class RemarkAdapter(val context: Context) :
    ListAdapter<Rework, RemarkAdapter.MyViewHolder>(DiffUtilRemarkDTO()) {
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
        fun bind(record: Rework, position: Int) {
            itemView.tvDescription.text = record.description
            itemView.tvType.text = record.type
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                itemView.tvStatus.delegate.backgroundColor = when (record.status) {
                    Constants.CONST_NOT_OK -> context.getColor(R.color.red)
                    Constants.CONST_OK -> context.getColor(R.color.green)
                    else -> context.getColor(R.color.green)
                }
            }
            itemView.tvStatus.text = record.status
            appOnClick?.let { _ ->
                itemView.tvStatus.setOnClickListener {
                    "click 0".printLog(javaClass.name)
                    appOnClick?.onClickListener(record, position)
                }
            }
        }
    }

}