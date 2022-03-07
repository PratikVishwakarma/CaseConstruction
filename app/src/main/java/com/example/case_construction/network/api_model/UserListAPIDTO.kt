package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

data class UserListAPIDTO(
    @SerializedName("data")
    val users: List<UserDTO>,
    @SerializedName("success")
    val status: Int,
    @SerializedName("message")
    val message: String
)