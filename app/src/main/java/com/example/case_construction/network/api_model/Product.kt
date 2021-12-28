package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Product : Serializable {
    @SerializedName("id")
    val id = ""

    @SerializedName("catid")
    val catId = ""

    @SerializedName("subcatid")
    val subCatId = ""

    @SerializedName("unitid")
    val unitId = ""

    @SerializedName("catname")
    val catName = ""

    @SerializedName("subcatname")
    val subCatName = ""

    @SerializedName("unitname")
    val unitName = ""

    @SerializedName("productbarcode")
    val productBarCode = ""

    @SerializedName("productmrp")
    val productMRP = "0.0"

    @SerializedName("offerid")
    val offerId = "0"

    @SerializedName("offerprice")
    val offerPrice = "0.0"

    @SerializedName("coupen_code")
    val couponCode = "0.0"

    @SerializedName("productsellingprice")
    var productSellingPrice = "0.0"

    @SerializedName("productopeningstock")
    val productOpeningStock = ""

    @SerializedName("productorderlimit")
    val productOrderLimit = ""

    @SerializedName("productdescription")
    val productDescription = ""

    @SerializedName("productname")
    val productName = ""// AMUL-CURD

    @SerializedName("productid")
    var productId = "" // 4

    @SerializedName("productimg")
    val productImg = ArrayList<Any>()

    @SerializedName("qty")
    var qty = "" // 4

    @SerializedName("subscriptionid")
    var subscriptionId = "0" // 4

    @SerializedName("rate")
    var rate = "" // 4

    @SerializedName("amount")
    var amount = "" // 4

    @SerializedName("status")
    var status = "" //

    var productAddedAt = Date()
}

