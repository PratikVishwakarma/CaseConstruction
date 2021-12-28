package com.example.case_construction.utility

import android.view.View

interface AppOnClick {
    fun onClickListener(item: Any, position: Int, view: View? = null)
}
