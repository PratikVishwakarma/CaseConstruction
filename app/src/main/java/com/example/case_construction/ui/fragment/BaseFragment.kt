@file:Suppress("DEPRECATION")

package com.example.case_construction.ui.fragment

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import androidx.transition.Fade
import androidx.transition.Slide
import com.example.case_construction.R
import com.example.case_construction.utility.Constants

open class BaseFragment : Fragment() {
    var bundle: Bundle? = null

    @VisibleForTesting
    val progressDialog by lazy {
        ProgressDialog(context)
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * To set message and show progress dialog
     * */
    fun showProgressDialog() {
        progressDialog.setMessage(getString(R.string.txt_loading))
        progressDialog.isIndeterminate = true
        progressDialog.setCancelable(false)
        progressDialog.show()
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * To hide progress dialog
     * */
    fun hideProgressDialog() {
        try {
            progressDialog.dismiss()
//            if (activity != null) (activity as MainActivity).hideProgressDialog()
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
        // First get FragmentManager object.
        val fragmentManager = fragmentManager ?: return
        // Begin Fragment transaction.
        val fragmentTransaction = fragmentManager.beginTransaction()

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(containerId, destFragment)

        // Commit the Fragment replace action.
        fragmentTransaction.commit()
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * Add destination Fragment on current fragment.
     * @param destFragment: Destination fragment
     * @param containerId: Id of container where fragment need to be shown
     * @param tag: Tag for the destination fragment
     * @param isFade: flag to show the fade animation on change or not
     * */
    fun addFragmentWithBack(
        destFragment: Fragment,
        containerId: Int,
        tag: String,
        isFade: Boolean = false
    ) {
        // First get FragmentManager object.
        val fragmentManager = fragmentManager ?: return
        // Begin Fragment transaction.
        val fragmentTransaction = fragmentManager.beginTransaction()
        if (!isFade) {
            val slide = Slide(Gravity.RIGHT)
            slide.duration = 200
            destFragment.enterTransition = slide
            destFragment.exitTransition = slide
        } else {
            val fade = Fade()
            fade.duration = 100
            fade.mode = Fade.IN
            destFragment.enterTransition = fade
            fade.mode = Fade.MODE_OUT
            destFragment.exitTransition = fade
        }
//        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.fade_out)
        // add the tag to the back stack.
        fragmentTransaction.addToBackStack(tag)

        // add the layout holder with the required Fragment object.
        fragmentTransaction.add(containerId, destFragment, tag)

        // Commit the Fragment add action.
        fragmentTransaction.commit()
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
            }, 750)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * ///////////////////////////////////////////////////////////////////////////////////////////////////
     * to redirect to the send mail or mail controller of android with subject and receiver email id
     * */
    fun goToContactSupport() {
        try {
            val emailIntent = Intent(
                Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", Constants.CONST_SUPPORT_MAIL_ID, null
                )
            )
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, Constants.CONST_SUPPORT_MAIL_SUBJECT)
//            emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
            startActivity(Intent.createChooser(emailIntent, "Choose an Email client :"))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun pauseClick(view: View){
        view.isClickable = true
        Handler(Looper.getMainLooper()).postDelayed({
            view.isClickable = true
        }, 150)
    }
}