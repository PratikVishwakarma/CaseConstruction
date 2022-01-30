package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Machine : Serializable {
    @SerializedName("id")
    var id = ""

    @SerializedName("machine_no")
    var machineNo = ""

    @SerializedName("okol_status")
    var okolStatus = ""

    @SerializedName("okol_status_date")
    var okolStatusDate = ""

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

