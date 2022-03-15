package com.example.case_construction.ui.fragment


import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.example.case_construction.R
import com.example.case_construction.adapter.MachineAdapter
import com.example.case_construction.model.ResultData
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.EnterMachineNoManuallyDialog
import com.example.case_construction.ui.machine.MachineViewModel
import com.example.case_construction.utility.*
import com.example.case_construction.utility.PreferenceHelper.currentUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_and_export_machine.*
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class SearchAndExportMachineFragment : BaseFragment() {
    private val machineViewModel by viewModels<MachineViewModel>()
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
        return inflater.inflate(R.layout.fragment_search_and_export_machine, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    @OptIn(DelicateCoroutinesApi::class)
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
//                        addDummyMachineNo()
                    }
                }
            ).show()
        }
        btnExport.setOnClickListener {
            if (machineList.isEmpty()) return@setOnClickListener
            it.pauseClick()
            GlobalScope.async {
                createExcel(
                    createWorkbook(
                        getDummyMachineData(),
                        (activity as MainActivity).defaultPreference.currentUser.userType
                    ), requireActivity()
                )
            }
        }
        initializeCustomerList()
//        getCustomerList()
//        swipeRefresh.setOnRefreshListener {
//            getMachineByNo()
//        }
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

    private fun initializeCustomerList() {
        mAdapter = MachineAdapter(
            requireContext(),
            (activity as MainActivity).defaultPreference.currentUser.userType
        )
        rvList.adapter = mAdapter
        mAdapter.appOnClick = object : AppOnClick {
            override fun onClickListener(item: Any, position: Int, view: View?) {
                goToHomePageAccordingToUserType()
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
                goToHomePageAccordingToUserType()
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
        when ((activity as MainActivity).defaultPreference.currentUser.userType) {
            Constants.CONST_USERTYPE_OKOL -> addFragmentWithBack(
                OKOLHomeFragment(),
                R.id.fragmentContainerView,
                "OKOLHomeFragment"
            )
            Constants.CONST_USERTYPE_TESTING -> addFragmentWithBack(
                TestingHomeFragment(),
                R.id.fragmentContainerView,
                "TestingHomeFragment"
            )
            Constants.CONST_USERTYPE_FINISHING -> addFragmentWithBack(
                FinishingHomeFragment(),
                R.id.fragmentContainerView, "FinishingHomeFragment"
            )
            Constants.CONST_USERTYPE_PDI_EXPORT, Constants.CONST_USERTYPE_PDI_DOMESTIC -> addFragmentWithBack(
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
                        swipeRefresh.isRefreshing = false
                        if (it.data == null) return@Observer
//                        machineList.clear()
                        machineList.addAll(it.data.machine)
                        initializeCustomerList()
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