package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class ProductListAPIDTO(
    @SerializedName("product")
    val products: List<Product>,
    @SerializedName("status")
    val status: Int
)