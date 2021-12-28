package com.example.case_construction.network.api_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserDTO : Serializable {

    @SerializedName("userid")
    var id = ""

  @SerializedName("customerno")
    var customerNo = ""

    @SerializedName("firstname")
    var firstName = ""

    @SerializedName("lastname")
    var lastName = ""

    @SerializedName("email")
    var email = ""

    @SerializedName("personreferral")
    var referral = ""

    @SerializedName("mobileno")
    var mobileNo = ""

    @SerializedName("personaddress")
    var address = ""

    @SerializedName("landmark")
    var landmark = ""

    @SerializedName("cityid")
    var cityId = ""

   @SerializedName("cityname")
    var cityName = ""

    @SerializedName("areaid")
    var areaId = ""

    @SerializedName("area")
    var area = ""

    @SerializedName("usertype")
    var userType = ""

    @SerializedName("isactive")
    var active = ""

    @SerializedName("userimage")
    var image = ""

    @SerializedName("_method")
    var method = ""
}


