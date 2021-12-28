package com.example.case_construction.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import com.example.case_construction.R
import com.example.case_construction.ui.dialog.ErrorDialog
import com.example.case_construction.ui.dialog.LoadingDialog
import com.example.case_construction.ui.dialog.NoInternetDialog
import com.example.case_construction.ui.fragment.SplashFragment
import com.example.case_construction.ui.onboarding.OnboardViewModel
import com.example.case_construction.utility.PreferenceHelper
import com.example.case_construction.utility.PreferenceHelper.currentUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.nav_header.view.*
import java.util.*


@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class MainActivity : BaseActivity() {

    companion object {
        const val CAMERA_PERMISSION_CODE = 100
        const val WRITE_EXTERNAL_STORAGE_PERMISSION_CODE = 101
    }

    lateinit var defaultPreference: SharedPreferences
    private var loadingDialog: LoadingDialog? = null
    private var noInternetDialog: NoInternetDialog? = null
    private var errorDialog: ErrorDialog? = null
    var actionBarDrawerToggle: ActionBarDrawerToggle? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        defaultPreference = PreferenceHelper.defaultPreference(this)
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = resources.getColor(R.color.colorSecondary)
        replaceFragment(SplashFragment(), R.id.fragmentContainerView)
        initView()
    }


    override fun onResume() {
        super.onResume()
        defaultPreference.registerOnSharedPreferenceChangeListener(listener)
    }

    override fun onPause() {
        super.onPause()
        defaultPreference.unregisterOnSharedPreferenceChangeListener(listener)
    }

    override fun onBackPressed() {
        if (my_drawer_layout.isOpen) {
            my_drawer_layout.closeDrawer(GravityCompat.START)
            return
        }
        super.onBackPressed()
    }


    private fun initView() {
        actionBarDrawerToggle =
            ActionBarDrawerToggle(this, my_drawer_layout, R.string.nav_open, R.string.nav_close);
        my_drawer_layout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)
        navView.setNavigationItemSelectedListener { menuItem ->
            val id = menuItem.itemId
            lockUnlockSideNav(LOCK_MODE_LOCKED_CLOSED)
            when (menuItem.itemId) {
                R.id.nav_logout -> {
                }
                R.id.nav_my_orders -> {
                }
                R.id.nav_subscriptions -> {
//
                }
                R.id.nav_complain -> {
                    if (checkPermission(
                            Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
                        ) &&
                        checkPermission(
                            Manifest.permission.CAMERA,
                            CAMERA_PERMISSION_CODE
                        )
                    ) {

                    }
                }
            }
            my_drawer_layout.closeDrawer(GravityCompat.START)
            true
        }
        updateUserDetailsOnSideNav()

    }

    fun goToLocation() {
        // Create a Uri from an intent string. Use the result to create an Intent.
        val gmmIntentUri = Uri.parse("google.navigation:q=22.95768044973039,76.03537295472357")

// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
// Make the Intent explicit by setting the Google Maps package
        mapIntent.setPackage("com.google.android.apps.maps")

// Attempt to start an activity that can handle the Intent
        startActivity(mapIntent)
    }

    fun logoutUser() {


    }

    fun updateUserDetailsOnSideNav() {
        navView.removeHeaderView(navView.getHeaderView(0));
        val navHeaderView =
            LayoutInflater.from(this).inflate(R.layout.nav_header, null)
        navView.addHeaderView(navHeaderView)
//        val navHeaderView = navView.inflateHeaderView(R.layout.nav_header)
        navHeaderView.tvUsername.text = defaultPreference.currentUser.firstName

        navHeaderView.setOnClickListener {
        }
    }

    private var listener: SharedPreferences.OnSharedPreferenceChangeListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->

        }


    fun hideShowProductButton(visibility: Int) {
        rllProductCart.visibility = visibility
    }


    fun openDrawer() {
        my_drawer_layout.openDrawer(GravityCompat.START)
    }

    fun lockUnlockSideNav(mode: Int) {
        my_drawer_layout.setDrawerLockMode(mode)
    }

    fun showLoadingDialog(message: String = "Loading") {
        loadingDialog?.hide()
        loadingDialog = LoadingDialog(this, message)
        loadingDialog?.show()
    }

    fun hideLoadingDialog() {
        loadingDialog?.hide()
    }

    fun showNoNetworkDialog(
        listener: NoInternetDialog.DialogListener
    ) {
        noInternetDialog?.hide()
        noInternetDialog = NoInternetDialog(this, listener)
        noInternetDialog?.show()
    }

    fun showErrorDialog(
        listener: ErrorDialog.DialogListener,
        message: String,
    ) {
        if (errorDialog != null) errorDialog?.hide()
        hideLoadingDialog()
        errorDialog = ErrorDialog(this, listener, message)
        errorDialog?.show()
    }

    // Function to check and request permission.
    fun checkPermission(permission: String, requestCode: Int): Boolean {
        return if (ContextCompat.checkSelfPermission(
                this@MainActivity,
                permission
            ) == PackageManager.PERMISSION_DENIED
        ) {
            // Requesting the permission
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(permission), requestCode)
            false
        } else true
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hideLoadingDialog()
                } else {
                    checkPermission(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        WRITE_EXTERNAL_STORAGE_PERMISSION_CODE
                    )
                    hideLoadingDialog()
                }
            }
            WRITE_EXTERNAL_STORAGE_PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    hideLoadingDialog()
                } else {
                    checkPermission(
                        Manifest.permission.CAMERA,
                        CAMERA_PERMISSION_CODE
                    )
                    hideLoadingDialog()
                }
            }
        }
    }

    fun getBaseURL(onboardViewModel: OnboardViewModel) {
//        onboardViewModel.getBaseURLDataVM()
//            .observe(this, Observer {
//                when (it) {
//                    is ResultData.Success -> {
//                        it.data?.let { data -> defaultPreference.baseData = data }
//                        onboardViewModel.getBaseURLDataVM().removeObservers(this)
//                    }
//                }
//            })
    }

}