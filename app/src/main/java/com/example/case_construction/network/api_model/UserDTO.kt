package com.example.case_construction.network.api_model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UserDTO : Serializable {

    @SerializedName("Id")
    var id = ""

  @SerializedName("UserName")
    var userName = ""

    @SerializedName("User_Type")
    var userType = ""

    @SerializedName("firstname")
    var firstName = ""

    @SerializedName("lastname")
    var lastName = ""

    @SerializedName("email")
    var email = ""

    @SerializedName("User_Active")
    var active = 0

    @SerializedName("userimage")
    var image = ""

    @SerializedName("_method")
    var method = ""
}


