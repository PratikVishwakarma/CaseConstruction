package com.example.case_construction.network.api_model

import com.google.gson.annotations.SerializedName


class SuccessAPIDTO(

    @SerializedName("message")
    val message: String,

    @SerializedName("userid")
    val userId: String,

    @SerializedName("success")
    val status: Int
)