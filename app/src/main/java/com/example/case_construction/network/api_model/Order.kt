package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Order : Serializable {
    @SerializedName("id")
    val id = ""

    @SerializedName("username")
    val userName = ""

    @SerializedName("orderdate")
    val date = ""

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

