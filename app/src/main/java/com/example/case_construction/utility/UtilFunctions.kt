package com.example.case_construction.utility

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.case_construction.R
import com.example.case_construction.model.UtilityDTO
import java.text.SimpleDateFormat
import java.util.*


fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun getImageByName(context: Context, name: String): Drawable? = try {
    val resources: Resources = context.resources
    val resourceId: Int = resources.getIdentifier(
        name, "drawable",
        context.packageName
    )
    resources.getDrawable(resourceId)
} catch (e: Exception) {
    e.printStackTrace()
    context.resources.getDrawable(R.mipmap.ic_launcher)
}

fun View.pauseClick() {
    this.isClickable = true
    Handler(Looper.getMainLooper()).postDelayed({
        this.isClickable = true
    }, 150)
}

fun String.printLog(className: String) {
    Log.e(className, this)
}

fun String.firstLetterToUpperCase(): String {
    return if (this.length > 1) (this[0].toUpperCase() + this.substring(1, this.lastIndex + 1))
    else this[0].toUpperCase().toString()
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.isInternetAvailable(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun String?.getImageUrl(category: String = ""): String {
//    if(!this.contains("/")){
//        return ""
//    }
//    val split = this.split("/")
//    split[0]
    if (this == null) return ""
    return "https://luckydairy.in/api/" + this.replace("../", "")
}

fun getDateFromString(date: String, format: String = "yyyy-MM-dd HH:mm:ss"): Date {
    return SimpleDateFormat(format, Locale.ENGLISH).parse(date)
}

fun getStringFromDate(date: Date, format: String = "MMM dd, yyyy"): String {
    Log.e("datetime", "${date.time}")
    return SimpleDateFormat(format, Locale.ENGLISH).format(date)
}

fun isToday(date: Date): Boolean {
    return Date().day == date.day
}

fun getDummyUtilData(): ArrayList<UtilityDTO> {
    val data: ArrayList<UtilityDTO> = ArrayList()
    data.add(UtilityDTO(false, "Heading 0", "Value 0", 8))
    data.add(UtilityDTO(false, "Heading 1", "Value 1", 8))
    data.add(UtilityDTO(false, "Heading 2", "Value 2", 8))
    data.add(UtilityDTO(false, "Heading 3", "Value 3", 8))
    data.add(UtilityDTO(false, "Heading 4", "Value 4", 8))
    data.add(UtilityDTO(false, "Heading 5", "Value 5", 8))
    data.add(UtilityDTO(false, "Heading 6", "Value 6", 8))
    data.add(UtilityDTO(false, "Heading 7", "Value 7", 8))
    data.add(UtilityDTO(false, "Heading 8", "Value 8", 8))
    data.add(UtilityDTO(false, "Heading 9", "Value 9", 8))
    return data
}
