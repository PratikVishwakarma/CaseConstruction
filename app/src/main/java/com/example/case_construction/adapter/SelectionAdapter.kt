package com.example.case_construction.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.case_construction.R
import com.example.case_construction.model.UtilityDTO
import com.example.case_construction.model.diffs.DiffUtilUtilityDTO
import com.example.case_construction.utility.AppOnClick
import kotlinx.android.synthetic.main.item_list_selection.view.*

@SuppressLint("SetTextI18n")
class SelectionAdapter :
    ListAdapter<UtilityDTO, SelectionAdapter.MyViewHolder>(DiffUtilUtilityDTO()) {
    var appOnClick: AppOnClick? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.item_list_selection, parent, false)
        return MyViewHolder(inflate)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(getItem(position), position)
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(record: UtilityDTO, position: Int) {
            itemView.tvText.text = record.value
            appOnClick?.let { _ ->
                itemView.setOnClickListener {
                    appOnClick?.onClickListener(record, position)
                }
            }
        }
    }

}