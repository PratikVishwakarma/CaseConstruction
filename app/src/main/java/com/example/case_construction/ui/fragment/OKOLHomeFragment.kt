package com.example.case_construction.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.case_construction.R
import com.example.case_construction.adapter.RemarkAdapter
import com.example.case_construction.model.ResultData
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.network.api_model.Rework
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.AddUpdateReworkDialog
import com.example.case_construction.ui.machine.MachineViewModel
import com.example.case_construction.utility.AppOnClick
import com.example.case_construction.utility.Constants
import com.example.case_construction.utility.PreferenceHelper.currentUser
import com.example.case_construction.utility.pauseClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_okol_home.*
import kotlinx.android.synthetic.main.fragment_okol_home.rtvAddRemark
import kotlinx.android.synthetic.main.fragment_search_machine.*

@AndroidEntryPoint
class OKOLHomeFragment : BaseFragment() {

    private lateinit var mAdapter: RemarkAdapter
    private val reworkList: ArrayList<Rework> = ArrayList()
    private val machineViewModel by viewModels<MachineViewModel>()
    private var mMachineNo = ""
    private var machine = Machine()

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
    }

    private fun initView() {
        (activity as MainActivity).lockUnlockSideNav(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
        mAdapter = RemarkAdapter(requireContext())
        rvList.adapter = mAdapter
        mAdapter.appOnClick = object : AppOnClick {
            override fun onClickListener(item: Any, position: Int, view: View?) {

            }
        }
        mAdapter.submitList(reworkList)

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
//        if (!requireContext().isInternetAvailable()) {
//            (activity as MainActivity).showNoNetworkDialog(object :
//                NoInternetDialog.DialogListener {
//                override fun onOkClick() {
//                    requireActivity().onBackPressed()
//                }
//            })
//            return
//        }
        getData()
    }

    private fun getData() {

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

    private fun removeAllObservable() {
//        viewModel.getCategoryListVM().removeObservers(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initView()
    }

}