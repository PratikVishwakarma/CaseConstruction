package com.example.case_construction.ui.fragment

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.print.PrintAttributes
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.print.PrintHelper
import com.example.case_construction.R
import com.example.case_construction.adapter.RemarkAdapter
import com.example.case_construction.model.ResultData
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.network.api_model.Rework
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.AddUpdateReworkDialog
import com.example.case_construction.ui.dialog.ErrorDialog
import com.example.case_construction.ui.dialog.ShowQrCodeDataDialog
import com.example.case_construction.ui.machine.MachineViewModel
import com.example.case_construction.utility.*
import com.example.case_construction.utility.PreferenceHelper.currentUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_okol_home.*
import org.json.JSONArray
import org.json.JSONObject

@AndroidEntryPoint
class OKOLHomeFragment : BaseFragment() {

    private lateinit var mAdapter: RemarkAdapter
    private val reworkList: ArrayList<Rework> = ArrayList()
    private val localReworkList: ArrayList<Rework> = ArrayList()
    private val machineViewModel by viewModels<MachineViewModel>()
    private var machine = Machine()
    private var isFirstTime = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        machine = bundle?.getSerializable(Constants.CONST_BUNDLE_DATA_1) as Machine
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_okol_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        getMachineByNo()
    }

    private fun initView() {
        (activity as MainActivity).lockUnlockSideNav(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        reworkList.clear()
        mAdapter = RemarkAdapter(requireContext())
        rvList.adapter = mAdapter
        mAdapter.appOnClick = object : AppOnClick {
            override fun onClickListener(item: Any, position: Int, view: View?) {
                "click 1".printLog(javaClass.name)
                when (view!!.id) {
                    R.id.tvStatus -> {
//                        if (machine.stage != (activity as MainActivity).defaultPreference.currentUser.userType) {
                            val rework = item as Rework
                            if (rework.status == Constants.CONST_NOT_OK) rework.status =
                                Constants.CONST_OK
                            else rework.status = Constants.CONST_NOT_OK
                            reworkList[position] = rework
                            if (rework.id == "") mAdapter.notifyItemChanged(position)
                            else {
                                updateRework(rework.id, rework.status)
                                mAdapter.notifyItemChanged(position)
                            }
//                        }
                    }
                }
            }
        }
        rtvAddRemark.setOnClickListener {
            it.pauseClick()
            AddUpdateReworkDialog(
                requireActivity(),
                object : AddUpdateReworkDialog.DialogListener {
                    override fun onUpdateClick(rework: Rework, type: String) {
                        reworkList.add(rework)
                        mAdapter.submitList(reworkList)
                        mAdapter.notifyItemInserted(reworkList.lastIndex)
                    }
                }
            ).show()
        }
        rtvOKOLHold.setOnClickListener {
            it.pauseClick()
            createReworkJson("HOLD")
        }
        rtvOKOLClear.setOnClickListener {
            it.pauseClick()
            createReworkJson("OK")
        }
        rtvNewRework.setOnClickListener {
            it.pauseClick()
            createReworkJson("OK")
        }
    }

    private fun setMachineView() {
        when {
            machine.stage == Constants.CONST_USERTYPE_PDI_GT -> {
                ErrorDialog(requireActivity(), object: ErrorDialog.DialogListener{
                    override fun onOkClick() {
                        requireActivity().onBackPressed()
                    }

                },
                    "The Machine already get Gate Ticket").show()
            }
            machine.stage != (activity as MainActivity).defaultPreference.currentUser.userType -> {
                reworkList.clear()
                reworkList.addAll(
                    machine.rework.filter { it.reworkFrom == (activity as MainActivity).defaultPreference.currentUser.userType && it.status == Constants.CONST_NOT_OK }
                )
                mAdapter.submitList(reworkList)
                llBottomButton.visibility = View.GONE
                rtvAddRemark.visibility = View.VISIBLE
                rtvNewRework.visibility = View.VISIBLE
            }
            else -> {
                llBottomButton.visibility = View.VISIBLE
                rtvAddRemark.visibility = View.VISIBLE
                rtvNewRework.visibility = View.GONE
                reworkList.clear()
                reworkList.addAll(
                    machine.rework.filter { it.reworkFrom == (activity as MainActivity).defaultPreference.currentUser.userType }
                )
                mAdapter.submitList(reworkList)
            }
        }
    }

    private fun createReworkJson(status: String, isNotUpdateStage: String = "0") {
        var jsonString = "[]"
        var newStatus = status
        val jsonArray = JSONArray()
        reworkList.forEach {
            if (it.id == "") {
                val jsonObject = JSONObject()
                jsonObject.put(
                    "userid",
                    (activity as MainActivity).defaultPreference.currentUser.id
                )
                jsonObject.put(
                    "reworkfrom",
                    (activity as MainActivity).defaultPreference.currentUser.userType
                )
                jsonObject.put("machineNo", machine.machineNo)
                jsonObject.put("reason", it.type)
                jsonObject.put("rework", it.description)
                jsonObject.put("status", it.status)
                jsonObject.put("shortageReason", it.shortageReason)
                jsonArray.put(jsonObject)
            }
        }
        if (jsonArray.length() != 0) jsonString = jsonArray.toString()
        reworkList.forEach {
            if((status == "OK" || status == "COK") && it.status == Constants.CONST_NOT_OK){
                newStatus = "COK"
            }
        }
        updateAndAddMachineStatusByNo(jsonString, newStatus, isNotUpdateStage)
    }

    private fun getMachineByNo() {
        machineViewModel.getMachineByNoVM(
            (activity as MainActivity).defaultPreference.currentUser.id,
            machine.machineNo,
            (activity as MainActivity).defaultPreference.currentUser.userType
        ).observe(viewLifecycleOwner, Observer {
                when (it) {
                    is ResultData.Loading -> {
                        (requireActivity() as MainActivity).showLoadingDialog()
                    }
                    is ResultData.Success -> {
                        (requireActivity() as MainActivity).hideLoadingDialog()
                        if (it.data == null) return@Observer
                        if(it.data.machine.isEmpty())return@Observer
                        machine = it.data.machine[0]
                        tvMachineNo.text = machine.machineNo
                        if(!isFirstTime){
                            val qrCodeData = getQRCodeData(
                                machine,
                                (activity as MainActivity).defaultPreference.currentUser.userType
                            )
                            "QRCODEDATA: ${qrCodeData.first}".printLog(javaClass.name)
                            "QRCODEDATA: ${qrCodeData.second}".printLog(javaClass.name)
                            val createQRCode = createQRCode(requireContext(), qrCodeData.first)
                            if(createQRCode != null){
                                ShowQrCodeDataDialog(
                                    requireActivity(),
                                    createQRCode,
                                    qrCodeData.second,
                                    object : ShowQrCodeDataDialog.DialogListener{
                                        override fun onDoneClick(bitmap: Bitmap) {
                                            activity?.also { context ->
                                                PrintHelper(context).apply {
                                                    scaleMode = PrintHelper.SCALE_MODE_FIT
                                                    orientation = PrintHelper.ORIENTATION_LANDSCAPE
                                                }.also { printHelper ->
                                                    printHelper.printBitmap("droids.jpg - test print", bitmap)
                                                }
                                            }
                                        }
                                    }
                                ).show()
                            }
                        }
                        isFirstTime = false
                        initView()
                        setMachineView()
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


    private fun updateAndAddMachineStatusByNo(reworkJSON: String, status: String, isNotUpdateStage: String) {
        "Rework array: $reworkJSON ".printLog(javaClass.name)
        "Rework Status: $status ".printLog(javaClass.name)
        "isNotUpdateStage : $isNotUpdateStage ".printLog(javaClass.name)
        machineViewModel.updateAndAddMachineStatusByNoVM(
            (activity as MainActivity).defaultPreference.currentUser.id,
            machine.machineNo,
            (activity as MainActivity).defaultPreference.currentUser.userType,
            status,
            reworkJSON,
            isNotUpdateStage
        ).observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Loading -> {
                    (requireActivity() as MainActivity).showLoadingDialog()
                }
                is ResultData.Success -> {
                    (requireActivity() as MainActivity).hideLoadingDialog()
                    getMachineByNo()
                    removeAllObservable()
                }
                is ResultData.NoContent -> {
                    (requireActivity() as MainActivity).hideLoadingDialog()
                    removeAllObservable()
                }
                is ResultData.Failed -> {
                    (requireActivity() as MainActivity).hideLoadingDialog()
                    removeAllObservable()
                }
                else -> {}
            }
        })
    }

    private fun updateRework(reworkId: String, status: String) {
        "Rework Status: $status ".printLog(javaClass.name)
        machineViewModel.updateReworkStatusByIdVM(
            (activity as MainActivity).defaultPreference.currentUser.id,
            reworkId,
            status,
            machine.machineNo
        ).observe(viewLifecycleOwner, Observer {
            when (it) {
                is ResultData.Loading -> {
                    (requireActivity() as MainActivity).showLoadingDialog("Updating Rework...")
                }
                is ResultData.Success -> {
                    (requireActivity() as MainActivity).hideLoadingDialog()
                    getMachineByNo()
                    removeAllObservable()
                }
                is ResultData.NoContent -> {
                    (requireActivity() as MainActivity).hideLoadingDialog()
                    removeAllObservable()
                }
                is ResultData.Failed -> {
                    (requireActivity() as MainActivity).hideLoadingDialog()
                    removeAllObservable()
                }
                else -> {}
            }
        })
    }

    private fun removeAllObservable() {
        machineViewModel.updateAndAddMachineStatusByNoVM("", "", "", "", "", "")
            .removeObservers(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initView()
    }

}