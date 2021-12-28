package com.example.case_construction.utility

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import com.example.case_construction.network.api_model.BaseURLAPIDTO
import com.example.case_construction.network.api_model.UserDTO
import com.google.gson.Gson

object
PreferenceHelper {

    val CURRENT_USER = "CURRENT_USER"
    val BASE_DATA = "BASE_DATA"

    fun defaultPreference(context: Context): SharedPreferences =
        PreferenceManager.getDefaultSharedPreferences(context)

    fun customPreference(context: Context, name: String): SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    private inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
        val editMe = edit()
        operation(editMe)
        editMe.apply()
    }


    var SharedPreferences.currentUser
        get() = run { Gson().fromJson(getString(CURRENT_USER, ""), UserDTO::class.java) }
        set(value) {
            editMe {
                it.putString(CURRENT_USER, Gson().toJson(value))
            }
        }

    var SharedPreferences.baseData
        get() = run { Gson().fromJson(getString(BASE_DATA, ""), BaseURLAPIDTO::class.java) }
        set(value) {
            editMe {
                it.putString(BASE_DATA, Gson().toJson(value))
            }
        }

    var SharedPreferences.clearValues
        get() = { }
        set(value) {
            editMe {
                it.putString(CURRENT_USER, "")
            }
        }

}