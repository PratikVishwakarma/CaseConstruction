package com.example.case_construction.model.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.case_construction.model.UtilityDTO
import com.example.case_construction.network.api_model.Machine

class DiffUtilMachine: DiffUtil.ItemCallback<Machine>() {
    override fun areItemsTheSame(oldItem: Machine, newItem: Machine): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Machine, newItem: Machine): Boolean {
        return oldItem == newItem
    }
}