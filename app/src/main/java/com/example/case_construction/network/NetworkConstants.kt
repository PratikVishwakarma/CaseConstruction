package com.example.case_construction.network

object NetworkConstants {

    const val BASE_URL = "https://luckydairy.in/api/entity/"
    const val URL_ALL_REQUIRED_DATA = "Users/commonAPIs.php"
    const val URL_CATEGORY = "Category"
    const val URL_SUB_CATEGORY = "SubCategory"
    const val URL_USER = "Users"
    const val URL_MOBILE_OTP = "Users/get_mobile_number.php/"
    const val URL_PRODUCT = "Product"
    const val URL_ORDER_MASTER_CREATE = "Ordermaster/create.php"
    const val URL_ORDER_MASTER = "Ordermaster"
    const val URL_SUBSCRIPTION_MASTER = "Subcriptionmaster"
    const val URL_UPDATE_SUBSCRIPTION = "Subcriptionmaster/update_subscription_status.php/"
    const val URL_CUSTOMER_All_ORDER_SUBSCRIPTION = "Users/get_user_order_sub_by_qrCode.php"
    const val URL_DAILY_DELIVERY = "Dailydelivery/create.php"
    const val URL_ORDER_DETAILS = "Orderdetails/get_order_details.php/"
    const val URL_OFFER = "Offer/"
    const val URL_COMPLAIN = "Complain/"


    const val INT_STATUS_SUCCESS = 200
    const val INT_STATUS_NO_DATA_AVAILABLE = 204

    const val USER_TYPE_CUSTOMER = "customer"
    const val SPLASH_WAIT = 3500L
}