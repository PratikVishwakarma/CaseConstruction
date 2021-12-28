package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class AllSubscriptionAPIDTO(
    @SerializedName("subcription")
    val subscription: List<Subscription>,

    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String
)