package com.example.case_construction.ui.fragment


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.case_construction.R
import com.example.case_construction.model.ResultData
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.EnterMachineNoManuallyDialog
import com.example.case_construction.ui.machine.MachineViewModel
import com.example.case_construction.utility.Constants
import com.example.case_construction.utility.PreferenceHelper.currentUser
import com.example.case_construction.utility.pauseClick
import com.example.case_construction.utility.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_machine.*
import kotlinx.coroutines.DelicateCoroutinesApi

@AndroidEntryPoint
class SearchMachineFragment : BaseFragment() {
    private val machineViewModel by viewModels<MachineViewModel>()
    private var mMachineNo = ""
    private var machine = Machine()
    private var codeScanner: CodeScanner? = null

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
            if (scannerView.visibility == View.VISIBLE)
                scannerView.visibility = View.GONE
            else checkForCameraPermission()
//            addDummyMachineNo(SCANNER)
        }
        ivLogout.setOnClickListener {
            it.pauseClick()
            (activity as MainActivity).finish()
//            if (scannerView.visibility == View.VISIBLE)
//                scannerView.visibility = View.GONE
//            else checkForCameraPermission()
//            addDummyMachineNo(SCANNER)
        }
        ivEnterCode.setOnClickListener {
            it.pauseClick()
            scannerView.visibility = View.GONE
            EnterMachineNoManuallyDialog(
                requireActivity(),
                object : EnterMachineNoManuallyDialog.DialogListener {
                    override fun onDoneClick(machineNo: String) {
                        mMachineNo = machineNo
                        getMachineByNo()
                    }
                }
            ).show()
        }
        rtvViewConfiguration.setOnClickListener {
            it.pauseClick()

            val fragment = ViewConfigurationFragment()
            bundle = Bundle().apply {
                putSerializable(Constants.CONST_BUNDLE_DATA_1, machine)
            }
            fragment.bundle = bundle
            fragment.setTargetFragment(this@SearchMachineFragment, 1212)
            addFragmentWithBack(
                fragment,
                R.id.fragmentContainerView,
                "ViewConfigurationFragment"
            )
        }
        rtvAddRemark.setOnClickListener {
            it.pauseClick()
            goToHomePageAccordingToUserType()
        }
    }
//
//    private fun addDummyMachineNo(from: String = MANUALLY) {
//        if (lastSearchFrom != from) {
//            machineList.clear()
//            mAdapter.notifyDataSetChanged()
//        }
//        lastSearchFrom = from
//        machineList.add(Machine().apply {
//            id = "${machineList.size + 1}"
//            machineNo = "XYZ_${machineList.size + 1}"
//            okolStatus = "OKOL Status ${machineList.size + 1}"
//            okolStatusDate = "OKOL Date ${machineList.size + 1}"
//        })
//        mAdapter.notifyItemInserted(machineList.lastIndex)
//    }

    private fun initializeScanner() {
        scannerView.visibility = View.VISIBLE
        if (codeScanner == null) codeScanner = CodeScanner(requireContext(), scannerView)
        codeScanner!!.decodeCallback = DecodeCallback {
            requireActivity().runOnUiThread {
                requireActivity().toast("Scan result: ${it.text}")
                log(javaClass.name, "Scan result: ${it.text}")
                mMachineNo = it.text
                getMachineByNo()
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

    private fun goToHomePageAccordingToUserType() {
        bundle = Bundle().apply {
            putSerializable(Constants.CONST_BUNDLE_DATA_1, machine)
        }
        when ((activity as MainActivity).defaultPreference.currentUser.userType) {
            Constants.CONST_USERTYPE_OKOL -> {
                val fragment = OKOLHomeFragment()
                fragment.bundle = bundle
                fragment.setTargetFragment(this@SearchMachineFragment, 1212)
                addFragmentWithBack(fragment, R.id.fragmentContainerView, "OKOLHomeFragment")
            }
            Constants.CONST_USERTYPE_TESTING -> {
                val fragment = TestingHomeFragment()
                fragment.bundle = bundle
                fragment.setTargetFragment(this@SearchMachineFragment, 1212)
                addFragmentWithBack(fragment, R.id.fragmentContainerView, "TestingHomeFragment")
            }
            Constants.CONST_USERTYPE_FINISHING -> {
                val fragment = FinishingHomeFragment()
                fragment.bundle = bundle
                fragment.setTargetFragment(this@SearchMachineFragment, 1212)
                addFragmentWithBack(fragment, R.id.fragmentContainerView, "FinishingHomeFragment")
            }
            Constants.CONST_USERTYPE_PDI -> {
                val fragment = FinishingHomeFragment()
                fragment.bundle = bundle
                fragment.setTargetFragment(this@SearchMachineFragment, 1212)
                addFragmentWithBack(fragment, R.id.fragmentContainerView, "FinishingHomeFragment")
            }
            else -> {
                val fragment = OKOLHomeFragment()
                fragment.bundle = bundle
                fragment.setTargetFragment(this@SearchMachineFragment, 1212)
                addFragmentWithBack(fragment, R.id.fragmentContainerView, "OKOLHomeFragment")
            }
        }
    }


    private fun getMachineByNo() {
        machineViewModel.getMachineByNoVM(
            (activity as MainActivity).defaultPreference.currentUser.id,
            mMachineNo,
            (activity as MainActivity).defaultPreference.currentUser.userType
        )
            .observe(viewLifecycleOwner, Observer {
                when (it) {
                    is ResultData.Loading -> {
                        (requireActivity() as MainActivity).showLoadingDialog()
                    }
                    is ResultData.Success -> {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        if (it.data == null) return@Observer
                        machine = it.data.machine[0]
                        llMiddleButtons.visibility = View.VISIBLE
                        machineViewModel.getMachineByNoVM("", "", "")
                            .removeObservers(requireActivity())
                    }
                    is ResultData.NoContent -> {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        machineViewModel.getMachineByNoVM("", "", "")
                            .removeObservers(requireActivity())
                    }
                    is ResultData.Failed -> {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        machineViewModel.getMachineByNoVM("", "", "")
                            .removeObservers(requireActivity())
                    }
                    else -> {}
                }
            })
    }
}