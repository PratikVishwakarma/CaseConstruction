package com.example.case_construction.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.example.case_construction.R
import com.example.case_construction.adapter.ConfigurationAdapter
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.NoInternetDialog
import com.example.case_construction.utility.AppOnClick
import com.example.case_construction.utility.Constants
import com.example.case_construction.utility.getDummyUtilData
import com.example.case_construction.utility.isInternetAvailable
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_configuration.*

@AndroidEntryPoint
class ViewConfigurationFragment : BaseFragment() {

    private lateinit var mAdapter: ConfigurationAdapter
    private var machine = Machine()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_configuration, container, false)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        machine = bundle?.getSerializable(Constants.CONST_BUNDLE_DATA_1) as Machine
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        tvMachineNo.text = machine.machineNo
        mAdapter = ConfigurationAdapter()
        rvList.adapter = mAdapter
        mAdapter.appOnClick = object : AppOnClick {
            override fun onClickListener(item: Any, position: Int, view: View?) {

            }
        }
        mAdapter.submitList(getDummyUtilData())

        if (!requireContext().isInternetAvailable()) {
            (activity as MainActivity).showNoNetworkDialog(object :
                NoInternetDialog.DialogListener {
                override fun onOkClick() {
                    requireActivity().onBackPressed()
                }
            })
            return
        }
        getData()
    }

    private fun getData() {

    }

    private fun removeAllObservable() {
//        viewModel.getCategoryListVM().removeObservers(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initView()
    }

}