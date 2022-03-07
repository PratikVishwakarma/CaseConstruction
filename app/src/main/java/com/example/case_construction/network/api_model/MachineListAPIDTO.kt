package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class MachineListAPIDTO(
    @SerializedName("data")
    val machine: List<Machine>,
    @SerializedName("success")
    val status: Int,
    @SerializedName("message")
    val message: String
)