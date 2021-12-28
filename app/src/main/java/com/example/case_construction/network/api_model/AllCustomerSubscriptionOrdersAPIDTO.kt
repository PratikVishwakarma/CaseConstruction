package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class AllCustomerSubscriptionOrdersAPIDTO(
    @SerializedName("orders")
    val orders: List<Order>,

    @SerializedName("subscription")
    val subscription: List<Subscription>,

    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String
)