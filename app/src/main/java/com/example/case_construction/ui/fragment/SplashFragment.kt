package com.example.case_construction.ui.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.example.case_construction.R
import com.example.case_construction.network.NetworkConstants
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.NoInternetDialog
import com.example.case_construction.ui.onboarding.LoginFragment
import com.example.case_construction.utility.PreferenceHelper
import com.example.case_construction.utility.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : BaseFragment() {
    private lateinit var defaultPreference: SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        defaultPreference = PreferenceHelper.defaultPreference(requireContext())
        initView()
    }

    private fun initView() {
        (activity as MainActivity).lockUnlockSideNav(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        if (!requireContext().isInternetAvailable()) {
            (activity as MainActivity).showNoNetworkDialog(object :
                NoInternetDialog.DialogListener {
                override fun onOkClick() {
                    requireActivity().onBackPressed()
                }
            })
            return
        }

        Handler().postDelayed({
//            val apply = UserDTO().apply {
//                userType = "okol"
//            }
//            (activity as MainActivity).defaultPreference.currentUser = apply
//            replaceFragment(SearchMachineFragment(), R.id.fragmentContainerView)
            replaceFragment(LoginFragment(), R.id.fragmentContainerView)
        }, NetworkConstants.SPLASH_WAIT)
    }
}