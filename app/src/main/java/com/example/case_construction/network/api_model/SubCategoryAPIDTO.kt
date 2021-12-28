package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class SubCategoryAPIDTO(
    @SerializedName("brand")
    val brand: List<Brand>,
    @SerializedName("subCategory")
    val subCategory: List<SubCategory>,
    @SerializedName("status")
    val status: Int,
    @SerializedName("message")
    val message: String
)