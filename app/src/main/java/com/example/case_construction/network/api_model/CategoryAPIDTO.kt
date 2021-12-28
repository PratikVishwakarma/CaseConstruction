package com.example.case_construction.network.api_model

import com.google.gson.annotations.SerializedName


class CategoryAPIDTO(

    @SerializedName("category")
    val category: List<Category>,

    @SerializedName("offerlist")
    val offers: List<Offer>,

    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String = "No data available"
)