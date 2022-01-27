package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Machine : Serializable {
    @SerializedName("id")
    val id = ""

    @SerializedName("machine_no")
    val machineNo = ""

    @SerializedName("okol_status")
    val okolStatus = ""

    @SerializedName("okol_status_date")
    val okolStatusDate = ""

    @SerializedName("productname")
    val productName = ""

    @SerializedName("qty")
    var qty = ""

    @SerializedName("rate")
    val rate = ""

    @SerializedName("orderno")
    val orderNo = ""

    @SerializedName("orderid")
    val orderId = ""

    @SerializedName("productimage")
    var productImage = ""

    @SerializedName("totalamount")
    val totalAmount = ""

    @SerializedName("orderstatus")
    val orderStatus = ""

    @SerializedName("ispaid")
    var paidStatus = "unpaid"

    @SerializedName("orderdetails")
    val orderDetails: List<Product> = ArrayList()
}

