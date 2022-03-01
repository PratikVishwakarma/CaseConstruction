@file:Suppress("DEPRECATION")

package com.example.case_construction.ui

import android.app.ProgressDialog
import android.content.Context
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.VisibleForTesting
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment


open class BaseActivity : AppCompatActivity() {
    init {
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl"
        )
        System.setProperty(
            "org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl"
        )
    }

    @VisibleForTesting
    val progressDialog by lazy {
        ProgressDialog(this)
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * To set message and show progress dialog
     * */
    fun showProgressDialog(text: String = "Loading...") {
        progressDialog.setMessage(text)
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.setCanceledOnTouchOutside(false)
        progressDialog.show()
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * To hide progress dialog
     * */
    fun hideProgressDialog() {
        progressDialog.dismiss()
    }

    fun hideKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun showKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInputFromWindow(view.windowToken, InputMethodManager.SHOW_IMPLICIT, 0)
    }

    public override fun onStop() {
        super.onStop()
        hideProgressDialog()
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * Add destination Fragment on current fragment.
     * @param destFragment: Destination fragment
     * @param containerId: Id of container where fragment need to be shown
     * @param tag: Tag for the destination fragment
     * */
    fun addFragmentWithBack(destFragment: Fragment, containerId: Int, tag: String) {
        try {
            // First get FragmentManager object.
            val fragmentManager = this.supportFragmentManager

            // Begin Fragment transaction.
            val fragmentTransaction = fragmentManager.beginTransaction()

            // add the tag to the back stack.
            fragmentTransaction.addToBackStack(tag)

            // add the layout holder with the required Fragment object.
            fragmentTransaction.add(containerId, destFragment, tag)

            // Commit the Fragment add action.
            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * Replace current Fragment with the destination Fragment.
     * @param destFragment: Destination fragment
     * @param containerId: Id of container where fragment need to be shown
     * */
    fun replaceFragment(destFragment: Fragment, containerId: Int) {
        try {
            // First get FragmentManager object.
            val fragmentManager = this.supportFragmentManager

            // Begin Fragment transaction.
            val fragmentTransaction = fragmentManager.beginTransaction()

            // Replace the layout holder with the required Fragment object.
            fragmentTransaction.replace(containerId, destFragment)

            // Commit the Fragment replace action.
            fragmentTransaction.commit()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * to Clear back stack of the fragment.
     * */
    fun clearBackStack() {
        try {
            val fm = this.supportFragmentManager
            for (i in 0 until fm.backStackEntryCount) {
                fm.popBackStack()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * the generic method to handle double/multi tap on the same view it will remove listener and reinitialize it in few milliseconds
     * @param v: view or element whose click interaction need to pause
     * @param viewListener: View listener of that particular view
     * */
    fun clickPause(v: View, viewListener: View.OnClickListener) {
        try {
            v.setOnClickListener(null)
            Handler().postDelayed({
                v.setOnClickListener(viewListener)
            }, 500)
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

    fun showToast(message: String){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}