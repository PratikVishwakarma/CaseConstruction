package com.example.case_construction.model.diffs

import androidx.recyclerview.widget.DiffUtil
import com.example.case_construction.model.UtilityDTO

class DiffUtilUtilityDTO: DiffUtil.ItemCallback<UtilityDTO>() {
    override fun areItemsTheSame(oldItem: UtilityDTO, newItem: UtilityDTO): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: UtilityDTO, newItem: UtilityDTO): Boolean {
        return oldItem == newItem
    }
}