package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class SubCategory(
    @SerializedName("subcatname")
    val subcatname: String, // Curd
    @SerializedName("subcategoryimg")
    val image: String, // Curd
    @SerializedName("subcatid")
    val subcatid: String, // 13
    @SerializedName("product")
    val product: List<Product>,
    @SerializedName("count")
    val count: Int // 2
)