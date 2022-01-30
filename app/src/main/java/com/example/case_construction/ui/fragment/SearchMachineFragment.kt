package com.example.case_construction.ui.fragment


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.*
import com.example.case_construction.R
import com.example.case_construction.adapter.MachineAdapter
import com.example.case_construction.adapter.RemarkAdapter
import com.example.case_construction.model.ResultData
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.network.api_model.UserDTO
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.EnterMachineNoManuallyDialog
import com.example.case_construction.utility.AppOnClick
import com.example.case_construction.utility.Constants
import com.example.case_construction.utility.PreferenceHelper.currentUser
import com.example.case_construction.utility.pauseClick
import com.example.case_construction.utility.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_okol_home.*
import kotlinx.android.synthetic.main.fragment_search_machine.*
import kotlinx.android.synthetic.main.fragment_search_machine.rvList

@AndroidEntryPoint
class SearchMachineFragment : BaseFragment() {
    //    private val vendorViewModel by viewModels<VendorViewModel>()
    private lateinit var mAdapter: MachineAdapter
    private var codeScanner: CodeScanner? = null
    private var mMachineNo = ""
    private val machineList = ArrayList<Machine>()
    private var lastSearchFrom = ""

    companion object {
        const val SCANNER = "scanner"
        const val MANUALLY = "manually"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_machine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        mMachineNo = ""
        scannerView.visibility = View.GONE
        ivScanner.setOnClickListener {
            it.pauseClick()
//            if (scannerView.visibility == View.VISIBLE)
//                scannerView.visibility = View.GONE
//            else checkForCameraPermission()
            addDummyMachineNo(SCANNER)
        }
        ivLogout.setOnClickListener {
            it.pauseClick()
            (activity as MainActivity).finish()
//            if (scannerView.visibility == View.VISIBLE)
//                scannerView.visibility = View.GONE
//            else checkForCameraPermission()
            addDummyMachineNo(SCANNER)
        }
        ivEnterCode.setOnClickListener {
            it.pauseClick()
            scannerView.visibility = View.GONE
            EnterMachineNoManuallyDialog(
                requireActivity(),
                object : EnterMachineNoManuallyDialog.DialogListener {
                    override fun onDoneClick(machineNo: String) {
                        mMachineNo = machineNo
                        addDummyMachineNo()
                    }
                }
            ).show()
        }
        initializeCustomerList()
//        getCustomerList()
        swipeRefresh.setOnRefreshListener {
            getCustomerList()
        }
    }

    private fun addDummyMachineNo(from: String = MANUALLY) {
        if (lastSearchFrom != from) {
            machineList.clear()
            mAdapter.notifyDataSetChanged()
        }
        lastSearchFrom = from
        machineList.add(Machine().apply {
            id = "${machineList.size + 1}"
            machineNo = "XYZ_${machineList.size + 1}"
            okolStatus = "OKOL Status ${machineList.size + 1}"
            okolStatusDate = "OKOL Date ${machineList.size + 1}"
        })
        mAdapter.notifyItemInserted(machineList.lastIndex)
    }

    private fun initializeCustomerList() {
        mAdapter = MachineAdapter()
        rvList.adapter = mAdapter
        mAdapter.appOnClick = object : AppOnClick {
            override fun onClickListener(item: Any, position: Int, view: View?) {
                goToSingleCustomerDeliveryList()
            }
        }
        mAdapter.submitList(machineList)
    }

    private fun initializeScanner() {
        scannerView.visibility = View.VISIBLE
        if (codeScanner == null) codeScanner = CodeScanner(requireContext(), scannerView)
        codeScanner!!.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                requireActivity().toast("Scan result: ${it.text}")
                log(javaClass.name, "Scan result: ${it.text}")
                mMachineNo = it.text
                goToSingleCustomerDeliveryList()
            }
        }
        codeScanner!!.startPreview()
    }

    private fun checkForCameraPermission() {
        if ((activity as MainActivity).checkPermission(
                Manifest.permission.CAMERA,
                MainActivity.CAMERA_PERMISSION_CODE
            )
        ) initializeScanner()
        else requireActivity().toast("Camera Permission Required")
    }

    private fun goToSingleCustomerDeliveryList() {
        when ((activity as MainActivity).defaultPreference.currentUser.userType) {
            "okol" -> addFragmentWithBack(
                OKOLHomeFragment(),
                R.id.fragmentContainerView,
                "OKOLHomeFragment"
            )
            "testing" -> addFragmentWithBack(
                TestingHomeFragment(),
                R.id.fragmentContainerView,
                "TestingHomeFragment"
            )
            "finishing" -> addFragmentWithBack(
                FinishingHomeFragment(),
                R.id.fragmentContainerView, "FinishingHomeFragment"
            )
            else -> addFragmentWithBack(
                OKOLHomeFragment(),
                R.id.fragmentContainerView,
                "OKOLHomeFragment"
            )
        }

    }


    private fun getCustomerList() {
        Log.e("Login ", "getOTP()")
//        vendorViewModel.getCustomerByMobileVM((activity as MainActivity).defaultPreference.currentUser.mobileNo)
//            .observe(viewLifecycleOwner, Observer {
//                when (it) {
//                    is ResultData.Loading -> {
//                        (requireActivity() as MainActivity).showLoadingDialog()
//                    }
//                    is ResultData.Success -> {
//                        (requireActivity() as MainActivity).hideLoadingDialog()
//                        swipeRefresh.isRefreshing = false
//                        if (it.data == null) return@Observer
//                        customerList.clear()
//                        customerList.addAll(it.data.customerList)
//                        initializeCustomerList()
//                        vendorViewModel.getCustomerByMobileVM("").removeObservers(requireActivity())
//                    }
//                    is ResultData.NoContent -> {
//                        (requireActivity() as MainActivity).hideLoadingDialog()
//                        vendorViewModel.getCustomerByMobileVM("").removeObservers(requireActivity())
//                    }
//                    is ResultData.Failed -> {
//                        (requireActivity() as MainActivity).hideLoadingDialog()
//                        vendorViewModel.getCustomerByMobileVM("").removeObservers(requireActivity())
//                    }
//                }
//            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (data != null && requestCode == 1212) {
            initView()
        }
    }
}