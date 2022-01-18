package com.example.case_construction.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.example.case_construction.R
import com.example.case_construction.adapter.ConfigurationAdapter
import com.example.case_construction.adapter.RemarkAdapter
import com.example.case_construction.model.UtilityDTO
import com.example.case_construction.network.api_model.Remark
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.ui.dialog.AddUpdateRemarkDialog
import com.example.case_construction.ui.dialog.NoInternetDialog
import com.example.case_construction.utility.AppOnClick
import com.example.case_construction.utility.isInternetAvailable
import com.example.case_construction.utility.pauseClick
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_okol_home.*

@AndroidEntryPoint
class OKOLHomeFragment : BaseFragment() {

    private lateinit var mAdapter: RemarkAdapter
    private val remarkList: ArrayList<Remark> = ArrayList()
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
        mAdapter = RemarkAdapter()
        rvList.adapter = mAdapter
        mAdapter.appOnClick = object : AppOnClick {
            override fun onClickListener(item: Any, position: Int, view: View?) {

            }
        }
        mAdapter.submitList(remarkList)

        rtvViewConfiguration.setOnClickListener {
            addFragmentWithBack(
                ViewConfigurationFragment(),
                R.id.fragmentContainerView,
                "ViewConfigurationFragment"
            )
        }
        rtvAddRemark.setOnClickListener {
            it.pauseClick()
            AddUpdateRemarkDialog(
                requireActivity(),
                object : AddUpdateRemarkDialog.DialogListener {
                    override fun onUpdateClick(remark: Remark, type: String) {
                        remarkList.add(remark)
                        mAdapter.submitList(remarkList)
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

    private fun removeAllObservable() {
//        viewModel.getCategoryListVM().removeObservers(requireActivity())
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        initView()
    }

}