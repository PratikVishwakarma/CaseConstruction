package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class OfferAPIDTO(
    @SerializedName("offer")
    val offer: List<Offer> = ArrayList(),

    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String
)