package com.example.case_construction.ui.fragment


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
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
import com.example.case_construction.utility.*
import com.example.case_construction.utility.PreferenceHelper.currentUser
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_okol_home.*
import kotlinx.android.synthetic.main.fragment_search_machine.*
import kotlinx.android.synthetic.main.fragment_search_machine.rvList
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
import kotlin.collections.ArrayList

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
        const val UPI_PAYMENT = 10101
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
        btnExport.setOnClickListener {
            doPayment()
//            if (machineList.isEmpty()) return@setOnClickListener
//            it.pauseClick()
//            GlobalScope.async {
//                createExcel(createWorkbook())
//            }
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
        when (requestCode) {
            UPI_PAYMENT -> if (Activity.RESULT_OK == resultCode || resultCode == 11) {
                if (data != null) {
                    val trxt = data.getStringExtra("response")
                    Log.d("UPI", "onActivityResult: $trxt")
                    val dataList = ArrayList<String>()
                    dataList.add(trxt ?: "Null value")
                    upiPaymentDataOperation(dataList)
                } else {
                    Log.d("UPI", "onActivityResult: " + "Return data is null")
                    val dataList = ArrayList<String>()
                    dataList.add("nothing")
                    upiPaymentDataOperation(dataList)
                }
            } else {
                Log.d(
                    "UPI",
                    "onActivityResult: " + "Return data is null"
                ) //when user simply back without payment
                val dataList = ArrayList<String>()
                dataList.add("nothing")
                upiPaymentDataOperation(dataList)
            }
        }
    }

    private fun upiPaymentDataOperation(data: ArrayList<String>) {

        var str: String? = data[0]
        Log.d("UPIPAY", "upiPaymentDataOperation: " + str!!)
        var paymentCancel = ""
        if (str == null) str = "discard"
        var status = ""
        var approvalRefNo = ""
        val response = str.split("&".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in response.indices) {
            val equalStr =
                response[i].split("=".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
            if (equalStr.size >= 2) {
                if (equalStr[0].toLowerCase() == "Status".toLowerCase()) {
                    status = equalStr[1].toLowerCase()
                } else if (equalStr[0].toLowerCase() == "ApprovalRefNo".toLowerCase() || equalStr[0].toLowerCase() == "txnRef".toLowerCase()) {
                    approvalRefNo = equalStr[1]
                }
            } else {
                paymentCancel = "Payment cancelled by user."
            }
        }

        when {
            status == "success" -> {
                //Code to handle successful transaction here.
                "Transaction successful.".toast(requireContext())
                Log.d("UPI", "responseStr: $approvalRefNo")
            }
            "Payment cancelled by user." == paymentCancel -> {
                "Payment cancelled by user.".toast(requireContext())
            }
            else -> {
                "Transaction failed.Please try again".toast(requireContext())
            }
        }
    }

    private fun doPayment() {
        val uri = Uri.parse("upi://pay").buildUpon()
            .appendQueryParameter("pa", "shivi104@ybl")
//            .appendQueryParameter("pa", "pratikvishwakarma00@ybl")
            .appendQueryParameter("pn", "Pratik")
            .appendQueryParameter("tn", "Note ")
            .appendQueryParameter("am", "10.0")
            .appendQueryParameter("cu", "INR")
            .build()
        val upiPayIntent = Intent(Intent.ACTION_VIEW)
        upiPayIntent.data = uri

        // will always show a dialog to user to choose an app
        val chooser = Intent.createChooser(upiPayIntent, "Pay with")

        // check if intent resolves
        if (null != chooser.resolveActivity(requireActivity().packageManager)) {
            startActivityForResult(chooser, UPI_PAYMENT)
        } else {
            "No UPI app found, please install one to continue".toast(requireContext())
        }

    }


    private fun createWorkbook(): Workbook {
        // Creating excel workbook
        val workbook = XSSFWorkbook()

        //Creating first sheet inside workbook
        //Constants.SHEET_NAME is a string value of sheet name
        val sheet: Sheet = workbook.createSheet("FactorySheet")

        //Create Header Cell Style
        val cellStyle = getHeaderStyle(workbook)

        //Creating sheet header row
        createSheetHeader(cellStyle, sheet)

        if (machineList.isNotEmpty()) {
            var index = 0
            //Adding data to the sheet
            addData(index, sheet)
            index++
            machineList.forEach {
                addData(index, sheet, it)
                index++
            }
        }

        return workbook
    }


    private fun createSheetHeader(cellStyle: CellStyle, sheet: Sheet) {
        //setHeaderStyle is a custom function written below to add header style

        //Create sheet first row
        val row = sheet.createRow(0)

        //Header list
        val HEADER_LIST = listOf("column_1", "column_2", "column_3")

        //Loop to populate each column of header row
        for ((index, value) in HEADER_LIST.withIndex()) {

            val columnWidth = (15 * 500)

            //index represents the column number
            sheet.setColumnWidth(index, columnWidth)

            //Create cell
            val cell = row.createCell(index)

            //value represents the header value from HEADER_LIST
            cell?.setCellValue(value)

            //Apply style to cell
            cell.cellStyle = cellStyle
        }
    }

    private fun getHeaderStyle(workbook: Workbook): CellStyle {

        //Cell style for header row
        val cellStyle: CellStyle = workbook.createCellStyle()

        //Apply cell color
        val colorMap: IndexedColorMap = (workbook as XSSFWorkbook).stylesSource.indexedColors
        var color = XSSFColor(IndexedColors.RED, colorMap).indexed
        cellStyle.fillForegroundColor = color
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)

        //Apply font style on cell text
        val whiteFont = workbook.createFont()
        color = XSSFColor(IndexedColors.WHITE, colorMap).indexed
        whiteFont.color = color
        whiteFont.bold = true
        cellStyle.setFont(whiteFont)


        return cellStyle
    }

    private fun addData(rowIndex: Int, sheet: Sheet) {

        //Create row based on row index
        val row = sheet.createRow(rowIndex)

        //Add data to each cell
        createCell(row, 0, "#ID") //Column 1
        createCell(row, 1, "Machine No.") //Column 2
        createCell(row, 2, "OKOL Status") //Column 3
        createCell(row, 3, "OKOL Date ") //Column 3

    }

    private fun addData(rowIndex: Int, sheet: Sheet, machine: Machine) {

        //Create row based on row index
        val row = sheet.createRow(rowIndex)

        //Add data to each cell
        createCell(row, 0, machine.id) //Column 1
        createCell(row, 1, machine.machineNo) //Column 2
        createCell(row, 2, machine.okolStatus) //Column 3
        createCell(row, 3, machine.okolStatusDate) //Column 3

    }

    private fun createCell(row: Row, columnIndex: Int, value: String?) {
        val cell = row.createCell(columnIndex)
        cell?.setCellValue(value)
    }

    private fun createExcel(workbook: Workbook) {

        //Get App Director, APP_DIRECTORY_NAME is a string
        val appDirectory = requireActivity().getExternalFilesDir("case_construction_sheet")
        appDirectory.toString().printLog(javaClass.name)
        if (appDirectory == null) {
            "0 Make dir ".printLog(javaClass.name)
        }
        //Check App Directory whether it exists or not, create if not.
        if (appDirectory != null && !appDirectory.exists()) {
            appDirectory.mkdirs()
            "1 Make dir ".printLog(javaClass.name)
        }

        try {
            "2 Make dir ".printLog(javaClass.name)
            //Create excel file with extension .xlsx
            val excelFile =
                File(appDirectory, SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".xlsx")
            excelFile.absolutePath.printLog(javaClass.name)

            //Write workbook to file using FileOutputStream

            val fileOut = FileOutputStream(excelFile)
            workbook.write(fileOut)
            fileOut.flush()
            fileOut.close()
            startFileShareIntent(excelFile.absolutePath)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        } catch (e: Exception) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun startFileShareIntent(filePath: String) { // pass the file path where the actual file is located.
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type =
                "*/*"  // "*/*" will accepts all types of files, if you want specific then change it on your need.
//            flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
//            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
//            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            putExtra(
                Intent.EXTRA_SUBJECT,
                "Sharing file from the AppName"
            )
            putExtra(
                Intent.EXTRA_TEXT,
                "Sharing file from the AppName with some description"
            )
            val fileURI = FileProvider.getUriForFile(
                context!!, context!!.packageName + ".fileprovider",
                File(filePath)
            )
            putExtra(Intent.EXTRA_STREAM, fileURI)
        }

        requireActivity().startActivity(Intent.createChooser(shareIntent, null))
    }
}