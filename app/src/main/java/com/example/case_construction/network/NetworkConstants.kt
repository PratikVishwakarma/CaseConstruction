package com.example.case_construction.network

object NetworkConstants {

    const val BASE_URL = "http://192.168.29.198/otpm/"
    const val URL_ALL_REQUIRED_DATA = "Users/commonAPIs.php"
    const val URL_LOGIN = "Account/LoginApi"
    const val URL_GET_MACHINE_BY_NO = "Machine/GetMachineNo"
    const val URL_UPDATE_REWORK_AND_STATUS_MACHINE_BY_NO = "Machine/CreateRework"
    const val URL_UPDATE_REWORK_BY_ID = "Machine/CreateRework"



    const val INT_STATUS_SUCCESS = 200
    const val INT_STATUS_NO_DATA_AVAILABLE = 204

    const val USER_TYPE_CUSTOMER = "customer"
    const val SPLASH_WAIT = 3500L
}