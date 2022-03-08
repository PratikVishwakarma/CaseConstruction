package com.example.case_construction.model.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.case_construction.network.api_model.Rework

class DiffUtilRemarkDTO: DiffUtil.ItemCallback<Rework>() {
    override fun areItemsTheSame(oldItem: Rework, newItem: Rework): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Rework, newItem: Rework): Boolean {
        return oldItem == newItem
    }
}