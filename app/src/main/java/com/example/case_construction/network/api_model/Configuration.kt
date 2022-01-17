package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Configuration(
    @SerializedName("id")
    val id: String, // 4

    @SerializedName("catname")
    val categoryName: String, // VEGITABLES

    @SerializedName("catimg")
    val categoryImage: String // VEGITABLES
) : Serializable