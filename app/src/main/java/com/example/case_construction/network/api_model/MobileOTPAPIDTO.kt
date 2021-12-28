package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class MobileOTPAPIDTO(
    @SerializedName("otp")
    val otp: String
)