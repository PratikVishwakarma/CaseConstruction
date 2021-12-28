package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class Brand(
    @SerializedName("brandname")
    val brandName: String, // AMUL
    @SerializedName("brandimg")
    val image: String, // AMUL
    @SerializedName("brandid")
    val brandId: String, // 1
    @SerializedName("product")
    val product: List<Product>,
    @SerializedName("count")
    val count: Int // 2
)