package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName

class Rework {
    @SerializedName("Id")
    val id = ""

    @SerializedName("reason")
    var type = ""

    @SerializedName("rework")
    var description = ""

    @SerializedName("reworkDate")
    var reworkDate = ""

    @SerializedName("reworkFrom")
    var reworkFrom = ""

    @SerializedName("status")
    var status = ""
}
