package com.example.case_construction.model.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.case_construction.model.UtilityDTO
import com.example.case_construction.network.api_model.Remark

class DiffUtilRemarkDTO: DiffUtil.ItemCallback<Remark>() {
    override fun areItemsTheSame(oldItem: Remark, newItem: Remark): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Remark, newItem: Remark): Boolean {
        return oldItem == newItem
    }
}