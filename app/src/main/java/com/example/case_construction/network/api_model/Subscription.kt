package com.example.case_construction.network.api_model


import com.example.case_construction.model.UtilityDTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Subscription : Serializable {
    @SerializedName("id")
    var id = ""

    @SerializedName("userId")
    var userId = ""

    @SerializedName("username")
    var userName = ""

    @SerializedName("firstname")
    var firstName = ""

    @SerializedName("lastname")
    var lastName = ""

    @SerializedName("date")
    var date = ""

    @SerializedName("startdate")
    var startDate = ""

    @SerializedName("enddate")
    var endDate = ""

    @SerializedName("subcriptionno")
    var subcriptionno = ""

    @SerializedName("totaldays")
    var totalDays = "0"

    @SerializedName("subcriptionstatus")
    var subcriptionstatus = ""

    @SerializedName("totalamount")
    var totalAmount = ""

    @SerializedName("productname")
    var productName = ""//

    @SerializedName("productid")
    var productId = ""

    @SerializedName("qty")
    var qty = "0"

    @SerializedName("rate")
    var rate = ""

    @SerializedName("skip_delivery")
    var skipDelivery = "0"

    @SerializedName("status")
    var status = ""

    @SerializedName("productimage")
    var productImage = ""

    @SerializedName("deliveryCharge")
    var deliveryCharge = UtilityDTO()

    @SerializedName("productimg")
    val productImg = ArrayList<Any>()

    var product = Product()
}

