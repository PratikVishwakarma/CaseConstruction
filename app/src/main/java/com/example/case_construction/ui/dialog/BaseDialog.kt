package com.example.case_construction.ui.dialog

import android.app.Activity
import android.app.Dialog
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import com.example.case_construction.R

/**
 * ///////////////////////////////////////////////////////////////////////////////////////////////////
 * this is the base dialog class with all the helper/common function required in custom dialog
 * @param _Activity: activity of the parent class from where dialog is called
 * */
open class BaseDialog(private val _Activity: Activity, theme: Int = R.style.MyThemeDialogSlide) :
    Dialog(_Activity, theme) {
    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * to initialize the dialog view like full screen, out side clicks and other
     * */
    fun initializeDialog() {
        try {

            this.window?.setLayout(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            this.window?.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
            this.setCancelable(true)
            this.setCanceledOnTouchOutside(true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * the generic method to handle double/multi tap on the same view it will remove listener and reinitialize it in few milliseconds
     * @param v: view or element whose click interaction need to pause
     * @param listener: View listener of that particular view
     * */
    fun clickPause(v: View, listener: View.OnClickListener) {
        try {
            v.setOnClickListener(null)
            Handler().postDelayed({
                v.setOnClickListener(listener)
            }, 1500)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * to print the log
     * @param className: name of class from where it is called
     * @param message: the message which need to be display/print
     * */
    fun log(className: String, message: String) {
        Log.i(className, message)
    }

}