package com.example.case_construction.ui.fragment


import android.Manifest
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.case_construction.ui.dialog.SelectDateDialog
import com.example.case_construction.ui.machine.MachineViewModel
import com.example.case_construction.utility.*
import com.example.case_construction.utility.PreferenceHelper.currentUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_search_and_export_machine.*
import kotlinx.android.synthetic.main.fragment_search_and_export_machine.ivEnterCode
import kotlinx.android.synthetic.main.fragment_search_and_export_machine.ivLogout
import kotlinx.android.synthetic.main.fragment_search_and_export_machine.ivScanner
import kotlinx.android.synthetic.main.fragment_search_and_export_machine.scannerView
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
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
        }
        ivLogout.setOnClickListener {
            it.pauseClick()
            SelectDateDialog(
                requireActivity(),
                object : SelectDateDialog.DialogListener{
                    override fun onDoneClick(date: String) {
                        "selected date $date".toast(requireContext())
                        getMachineData(date)
                    }

                }
            ).show()
        }
        ivEnterCode.setOnClickListener {
            it.pauseClick()
            scannerView.visibility = View.GONE
            EnterMachineNoManuallyDialog(
                requireActivity(),
                object : EnterMachineNoManuallyDialog.DialogListener {
                    override fun onDoneClick(machineNo: String) {
                        mMachineNo = machineNo
                        if(!checkIfMachineExist()) return
                        getMachineData()
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
                        machineList,
                        (activity as MainActivity).defaultPreference.currentUser.userType
                    ), requireActivity()
                )
            }
        }
        initializeMachineList()
        swipeRefresh.setOnRefreshListener {
            swipeRefresh.isRefreshing=false
        }
    }

    private fun initializeMachineList() {
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
                log(javaClass.name, "Scan result: ${it.text}")
                mMachineNo = it.text
                if(!checkIfMachineExist()) return@runOnUiThread
                getMachineData()
                scannerView.visibility = View.GONE
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


    private fun getMachineData(date:String = "") {
        var machineByNoVM = machineViewModel.getMachineByNoVM(
            (activity as MainActivity).defaultPreference.currentUser.id,
            mMachineNo,
            (activity as MainActivity).defaultPreference.currentUser.userType
        )
        if(date.isNotBlank())
            machineByNoVM = machineViewModel.getMachineByStatusAndDate(
                (activity as MainActivity).defaultPreference.currentUser.id,
                mMachineNo,
                (activity as MainActivity).defaultPreference.currentUser.userType,
                date
            )
        machineByNoVM.observe(viewLifecycleOwner, Observer {
                when (it) {
                    is ResultData.Loading -> {
                        (requireActivity() as MainActivity).showLoadingDialog()
                    }
                    is ResultData.Success -> {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        swipeRefresh.isRefreshing = false
                        if (it.data == null) return@Observer
                        if(it.data.machine.isEmpty()) return@Observer
                        checkPreInsertCondition(it.data.machine)
                        machineByNoVM.removeObservers(requireActivity())
                    }
                    is ResultData.NoContent -> {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        machineByNoVM.removeObservers(requireActivity())
                    }
                    is ResultData.Failed -> {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        machineByNoVM.removeObservers(requireActivity())
                    }
                    else -> {}
                }
            })
    }

    private fun checkIfMachineExist() : Boolean {
        val filter = machineList.filter { it.machineNo == mMachineNo }
        return filter.isEmpty()
    }


    private fun checkPreInsertCondition(machines:List<Machine>){
        val preSize = machineList.size
        machines.forEach { machine ->
            var isExist = false
            machineList.forEach {
                if(it.machineNo == machine.machineNo){
                    isExist = true
                    return@forEach
                }
            }
            if(isExist) return@forEach
            if(!machineList.contains(machine))
                when ((activity as MainActivity).defaultPreference.currentUser.userType) {
                    Constants.CONST_USERTYPE_OKOL -> machineList.add(machine)
                    Constants.CONST_USERTYPE_TESTING ->
                        if (machine.testingStatus.isNotBlank()) machineList.add(machine)
                    Constants.CONST_USERTYPE_FINISHING ->
                        if (machine.finishStatus.isNotBlank()) machineList.add(machine)
                    Constants.CONST_USERTYPE_PDI_EXPORT,
                    Constants.CONST_USERTYPE_PDI_DOMESTIC,
                    Constants.CONST_USERTYPE_PDI_GT -> if (machine.pdiStatus.isNotBlank()) machineList.add(
                        machine
                    )
                }
        }
        if(preSize == machineList.size)
            "Machine is not yet on your line".toast(requireContext())
        else initializeMachineList()

    }

}