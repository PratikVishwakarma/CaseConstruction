package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Offer(
    @SerializedName("id")
    val id: String = "",

    @SerializedName("offername")
    val offerName: String = "",

    @SerializedName("coupencode")
    val coupenCode: String = "",

    @SerializedName("offertype")
    val offerType: String = "",

    @SerializedName("max_offer_amount")
    val discountedAmmount: String = ""
) : Serializable