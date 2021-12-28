package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class AllOrdersAPIDTO(
    @SerializedName("orders")
    val orders: List<Order>,

    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String
)