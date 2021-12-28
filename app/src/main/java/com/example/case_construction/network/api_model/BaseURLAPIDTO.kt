package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class BaseURLAPIDTO(

    @SerializedName("status")
    val status: Int,

    @SerializedName("message")
    val message: String
)