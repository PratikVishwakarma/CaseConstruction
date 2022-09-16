package com.example.case_construction.network

object NetworkConstants {
    //otpm/
    const val BASE_URL = "http://192.168.1.10:90/" //WiFi Url
    //const val BASE_URL = "http://141.86.185.81:97/" // Case Url
//    const val BASE_URL = "http://192.168.100.45/" // Mobile Data Url
    const val URL_ALL_REQUIRED_DATA = "Users/commonAPIs.php"
    const val URL_LOGIN = "Account/LoginApi"
    const val URL_GET_MACHINE_BY_NO = "Machine/GetMachineNo"
    const val URL_GET_MACHINE_BY_DATE = "Machine/GetMachineNoByDate"
    const val URL_UPDATE_REWORK_AND_STATUS_MACHINE_BY_NO = "Machine/CreateRework"
    const val URL_UPDATE_REWORK_BY_ID = "Machine/reworkStatusUpdate"



    const val INT_STATUS_SUCCESS = 200
    const val INT_STATUS_NO_DATA_AVAILABLE = 204

    const val USER_TYPE_CUSTOMER = "customer"
    const val SPLASH_WAIT = 3500L
}