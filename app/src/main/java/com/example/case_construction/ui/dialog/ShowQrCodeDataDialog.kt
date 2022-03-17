package com.example.case_construction.ui.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.view.Window
import com.example.case_construction.R
import kotlinx.android.synthetic.main.dialog_qr_code_show.*

@SuppressLint("SetTextI18n")
class ShowQrCodeDataDialog(
    val activity: Activity,
    private val bitmap: Bitmap,
    val text: String,
    private val listener: DialogListener,
) : BaseDialog(activity, R.style.MyThemeDialogSelect), View.OnClickListener {
    var selectedDate = ""

    interface DialogListener {
        fun onDoneClick(bitmap: Bitmap)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.dialog_qr_code_show)
        initializeDialog()
        tvDone.setOnClickListener(this)
        ivQRCode.setImageBitmap(bitmap)
        tvText.text = text

    }


    override fun onClick(v: View?) {
        try {
            if (v != null) clickPause(v, this) else return
            when (v) {
                tvDone -> {
                    try {
                        // create bitmap screen capture
                             val v1= llMain
//                        val v1 = window!!.decorView.rootView
                        v1.isDrawingCacheEnabled = true
                        val bitmap = Bitmap.createBitmap(v1.drawingCache)
                        v1.isDrawingCacheEnabled = false
                        listener.onDoneClick(bitmap)
                        this.dismiss()
                    } catch (e: Exception) {
                        e.printStackTrace()
                        this.dismiss()
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


}