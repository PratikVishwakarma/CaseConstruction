package com.example.case_construction.utility

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Display
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.core.content.FileProvider
import com.example.case_construction.R
import com.example.case_construction.model.UtilityDTO
import com.example.case_construction.network.api_model.Machine
import com.example.case_construction.network.api_model.Rework
import com.example.case_construction.ui.MainActivity
import com.example.case_construction.utility.PreferenceHelper.currentUser
import com.google.zxing.WriterException
import kotlinx.android.synthetic.main.fragment_login.*
import org.apache.poi.ss.usermodel.*
import org.apache.poi.xssf.usermodel.IndexedColorMap
import org.apache.poi.xssf.usermodel.XSSFColor
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.xmlbeans.UserType
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


fun String.toast(context: Context, duration: Int = Toast.LENGTH_SHORT): Toast {
    return Toast.makeText(context, this, duration).apply { show() }
}

@SuppressLint("UseCompatLoadingForDrawables")
fun getImageByName(context: Context, name: String): Drawable? = try {
    val resources: Resources = context.resources
    val resourceId: Int = resources.getIdentifier(
        name, "drawable",
        context.packageName
    )
    resources.getDrawable(resourceId)
} catch (e: Exception) {
    e.printStackTrace()
    context.resources.getDrawable(R.mipmap.ic_launcher)
}

fun View.pauseClick() {
    this.isClickable = true
    Handler(Looper.getMainLooper()).postDelayed({
        this.isClickable = true
    }, 150)
}

fun String.printLog(className: String) {
    Log.e(className, this)
}

fun String.firstLetterToUpperCase(): String {
    return if (this.length > 1) (this[0].toUpperCase() + this.substring(1, this.lastIndex + 1))
    else this[0].toUpperCase().toString()
}

fun Context.toast(message: String, length: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, length).show()
}

fun Context.isInternetAvailable(): Boolean {
    val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    return activeNetwork?.isConnectedOrConnecting == true
}

fun String?.getImageUrl(category: String = ""): String {
//    if(!this.contains("/")){
//        return ""
//    }
//    val split = this.split("/")
//    split[0]
    if (this == null) return ""
    return "https://luckydairy.in/api/" + this.replace("../", "")
}

fun getDateFromString(date: String, format: String = "yyyy-MM-dd HH:mm:ss"): Date {
    return SimpleDateFormat(format, Locale.ENGLISH).parse(date)
}

fun getStringFromDate(date: Date, format: String = "MMM dd, yyyy"): String {
    Log.e("datetime", "${date.time}")
    return SimpleDateFormat(format, Locale.ENGLISH).format(date)
}

fun isToday(date: Date): Boolean {
    return Date().day == date.day
}

fun getReworkTypeDataList(): ArrayList<UtilityDTO> {
    val data: ArrayList<UtilityDTO> = ArrayList()
    data.add(UtilityDTO(false, "Shortage", "Shortage", 0))
    data.add(UtilityDTO(false, "Assembly", "Assembly", 1))
    data.add(UtilityDTO(false, "Quality", "Quality", 2))
    data.add(UtilityDTO(false, "Paintshop", "Paintshop", 3))
    data.add(UtilityDTO(false, "Fabrication", "Fabrication", 4))
    data.add(UtilityDTO(false, "ME", "ME", 5))
    data.add(UtilityDTO(false, "Design", "Design", 6))
    return data
}


fun getDummyMachineData(): ArrayList<Machine> {
    val data: ArrayList<Machine> = ArrayList()
    val machine = Machine()
    machine.machineNo = "123"
    machine.oKOLStatus = "OK"
    machine.oKOLDate = "10-03-2022"
    machine.testingStatus = "OK"
    machine.testingDate = "10-03-2022"
    machine.finishStatus = "OK"
    machine.finishDate = "10-03-2022"
    machine.pdiStatus = "GT"
    machine.rework.add(Rework().apply {
        this.type = "Value 1"
        this.description = "${Constants.CONST_USERTYPE_OKOL} 1st rework"
        this.reworkFrom = Constants.CONST_USERTYPE_OKOL
        this.status = "Ok"
    })
    machine.rework.add(Rework().apply {
        this.type = "Value 1"
        this.description = "${Constants.CONST_USERTYPE_OKOL} 2nd rework"
        this.reworkFrom = Constants.CONST_USERTYPE_OKOL
        this.status = "Not Ok"
    })
    machine.rework.add(Rework().apply {
        this.type = "Value 1"
        this.description = "${Constants.CONST_USERTYPE_TESTING}  1st rework"
        this.reworkFrom = Constants.CONST_USERTYPE_TESTING
        this.status = "Ok"
    })
    machine.rework.add(Rework().apply {
        this.type = "Value 1"
        this.description = "${Constants.CONST_USERTYPE_TESTING} 2nd rework"
        this.reworkFrom = Constants.CONST_USERTYPE_TESTING
        this.status = "Not Ok"
    })
    machine.rework.add(Rework().apply {
        this.type = "Value 1"
        this.description = "${Constants.CONST_USERTYPE_FINISHING} 1st rework"
        this.reworkFrom = Constants.CONST_USERTYPE_FINISHING
        this.status = "Ok"
    })
    machine.rework.add(Rework().apply {
        this.type = "Value 1"
        this.description = "${Constants.CONST_USERTYPE_FINISHING} 2nd rework"
        this.reworkFrom = Constants.CONST_USERTYPE_FINISHING
        this.status = "Not Ok"
    })
    machine.rework.add(Rework().apply {
        this.type = "Value 1"
        this.description = "$Constants.CONST_USERTYPE_PDI_DOMESTIC 1st rework"
        this.reworkFrom = Constants.CONST_USERTYPE_PDI_DOMESTIC
        this.status = "Ok"
    })
    machine.rework.add(Rework().apply {
        this.type = "Value 1"
        this.description = "$Constants.CONST_USERTYPE_PDI_DOMESTIC 2nd rework"
        this.reworkFrom = Constants.CONST_USERTYPE_PDI_DOMESTIC
        this.status = "Not Ok"
    })
    data.add(machine)
    data.add(machine)
    return data
}

fun getConfigurationData(machine: Machine): ArrayList<UtilityDTO> {
    val data: ArrayList<UtilityDTO> = ArrayList()
    data.add(UtilityDTO(false, "Days Plan", machine.daysPlan, 8))
    data.add(UtilityDTO(false, "Machine No", machine.machineNo, 8))
    data.add(UtilityDTO(false, "Line-Up Date", machine.lineUpDate, 8))
    data.add(UtilityDTO(false, "Model No", machine.modelNo, 8))
    data.add(UtilityDTO(false, "Chassis", machine.chassis, 8))
    data.add(UtilityDTO(false, "Drive", machine.drive, 8))
    data.add(UtilityDTO(false, "LDR Valve", machine.ldrValve, 8))
    data.add(UtilityDTO(false, "RTD", machine.rtd, 8))
    data.add(UtilityDTO(false, "Hammer", machine.hammer, 8))
    data.add(UtilityDTO(false, "Telematics", machine.telematics, 8))
    data.add(UtilityDTO(false, "BH Valve", machine.bhValve, 8))
    data.add(UtilityDTO(false, "BH Pattern", machine.bhPattern, 8))
    data.add(UtilityDTO(false, "Engine", machine.engine, 8))
    data.add(UtilityDTO(false, "Counter Weight", machine.counterWeight, 8))
    data.add(UtilityDTO(false, "Loader Arm", machine.loaderArm, 8))
    data.add(UtilityDTO(false, "Cabin", machine.cabin, 8))
    data.add(UtilityDTO(false, "Beacon", machine.beacon, 8))
    data.add(UtilityDTO(false, "Fender", machine.fender, 8))
    data.add(UtilityDTO(false, "Dipper", machine.dipper, 8))
    data.add(UtilityDTO(false, "BH Bucket", machine.bhBucket, 8))
    data.add(UtilityDTO(false, "LDR Bucket", machine.ldrBucket, 8))
    data.add(UtilityDTO(false, "Tyre Front", machine.tyreFront, 8))
    data.add(UtilityDTO(false, "Tyre Rear", machine.tyreRear, 8))
    data.add(UtilityDTO(false, "Lenguage", machine.lenguage, 8))
    data.add(UtilityDTO(false, "Market", machine.market, 8))
    return data
}

fun getQRCodeData(machine: Machine, userType: String): Pair<String, String> {
    var userTypePDI = userType
    if (userType == Constants.CONST_USERTYPE_PDI_EXPORT || userType == Constants.CONST_USERTYPE_PDI_DOMESTIC)
        userTypePDI = "PDI"
    var bottomText = ""
    var qrText = machine.machineNo + "-"
    when(userTypePDI){
        Constants.CONST_USERTYPE_OKOL -> {
            bottomText = "${machine.machineNo}${machine.oKOLStatus}${getDateForPrint(machine.oKOLDate)}-${getDateForPrint(machine.lineUpDate)}"
            qrText += "${machine.oKOLStatus}${getDateForPrint(machine.oKOLDate)}-"
        }
        Constants.CONST_USERTYPE_TESTING -> {
            bottomText = "${machine.machineNo}${machine.testingStatus}${getDateForPrint(machine.testingDate)}-${getDateForPrint(machine.lineUpDate)}"
            qrText += "${machine.testingStatus}${getDateForPrint(machine.testingDate)}-"
        }
        Constants.CONST_USERTYPE_FINISHING -> {
            bottomText = "${machine.machineNo}${machine.finishStatus}${getDateForPrint(machine.finishDate)}-${getDateForPrint(machine.lineUpDate)}"
            qrText += "${machine.finishStatus}${getDateForPrint(machine.finishDate)}-"
        }
        Constants.CONST_USERTYPE_PDI_DOMESTIC, Constants.CONST_USERTYPE_PDI_EXPORT, Constants.CONST_USERTYPE_PDI_GT -> {
            bottomText = "${machine.machineNo}${machine.pdiStatus}${getDateForPrint(machine.pdiDate)}-${getDateForPrint(machine.lineUpDate)}"
            qrText += "${machine.pdiStatus}${getDateForPrint(machine.pdiDate)}-"
        }
    }
    qrText += getReworkText(machine.rework.filter { it.reworkFrom == userTypePDI })
    return Pair(qrText, bottomText)
}

fun getDateForPrint(date: String): String{
    return try {
        val split = date.split("-")

        "date : $date".printLog("Utilfunctions")
        val last = split.last().drop(2)
        "show date: ${split[0]}${split[1]}$last".printLog("Utilfunctions")
        "${split[0]}${split[1]}$last"
    } catch (e: Exception) {
        date.replace("-","")
    }

}

fun getReworkText(reworks: List<Rework>): String{
    var sRework = ""
    reworks.forEach {
        sRework += it.description
        sRework += if(it.status == Constants.CONST_NOT_OK) " not ok, "
        else ", "
    }
    return sRework.trim().dropLast(1)
}

fun createWorkbook(machineList: ArrayList<Machine>, userType: String): Workbook {
    // Creating excel workbook
    val workbook = XSSFWorkbook()

    //Creating first sheet inside workbook
    //Constants.SHEET_NAME is a string value of sheet name
    val sheet: Sheet = workbook.createSheet("FactorySheet")

    //Create Header Cell Style
    val cellStyle = getHeaderStyle(workbook)

    if (machineList.isNotEmpty()) {
        var index = 0
        //Creating sheet header row
        createSheetHeader(cellStyle, sheet)
        //Adding data to the sheet
//        addData(index, sheet, userType)
        index++
        machineList.forEach {
            addData(index, sheet, userType, it, workbook)
            index++
            index = addBlankReworkData(index, sheet, userType, it, workbook)
        }
    }

    return workbook
}


private fun createSheetHeader(cellStyle: CellStyle, sheet: Sheet) {
    //setHeaderStyle is a custom function written below to add header style

    //Create sheet first row
    val row = sheet.createRow(0)

    //Header list
    val HEADER_LIST = listOf("Machine No.", "Status", "Date", "Rework")

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

private fun getOkRemarkStyle(workbook: Workbook, statusColor: IndexedColors): CellStyle {
    //Cell style for header row
    val cellStyle: CellStyle = workbook.createCellStyle()

    //Apply cell color
    val colorMap: IndexedColorMap = (workbook as XSSFWorkbook).stylesSource.indexedColors
    var color = XSSFColor(IndexedColors.RED, colorMap).indexed
//    cellStyle.fillForegroundColor = color
//    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND)

    //Apply font style on cell text
    val whiteFont = workbook.createFont()
    color = XSSFColor(statusColor, colorMap).indexed
    whiteFont.color = color
    if(statusColor != IndexedColors.BLACK) whiteFont.bold = true
    cellStyle.setFont(whiteFont)
    return cellStyle
}

private fun addData(
    rowIndex: Int,
    sheet: Sheet,
    userType: String,
    machine: Machine,
    workbook: Workbook
) {

    //Create row based on row index
    val row = sheet.createRow(rowIndex)

    var value1 = ""
    var value2 = ""
    when (userType) {
        Constants.CONST_USERTYPE_OKOL -> {
            value1 = machine.oKOLStatus
            value2 = machine.oKOLDate
        }
        Constants.CONST_USERTYPE_TESTING -> {
            value1 = machine.testingStatus
            value2 = machine.testingDate
        }
        Constants.CONST_USERTYPE_FINISHING -> {
            value1 = machine.finishStatus
            value2 = machine.finishDate
        }
        Constants.CONST_USERTYPE_PDI_DOMESTIC, Constants.CONST_USERTYPE_PDI_EXPORT, Constants.CONST_USERTYPE_PDI_GT -> {
            value1 = machine.pdiStatus
            value2 = machine.pdiDate
        }
    }
    //Add data to each cell
    createCell(row, 0, machine.machineNo, workbook = workbook) //Column 2
    createCell(row, 2, value2, workbook = workbook) //Column 3
    createCell(row, 3, "", workbook = workbook) //Column 3
    if (value1 == Constants.CONST_NOT_OK)
        createCell(row, 1, value1, IndexedColors.RED, workbook)
    else
        createCell(row, 1, value1, IndexedColors.GREEN, workbook)
}

private fun addBlankReworkData(
    rowIndex: Int,
    sheet: Sheet,
    userType: String,
    machine: Machine,
    workbook: Workbook
): Int {
    var index = rowIndex
    var userTypePDI = userType
    if (userType == Constants.CONST_USERTYPE_PDI_EXPORT || userType == Constants.CONST_USERTYPE_PDI_DOMESTIC)
        userTypePDI = "PDI"
    var value1 = ""
    var value2 = ""
    when (userType) {
        Constants.CONST_USERTYPE_OKOL -> {
            value1 = machine.oKOLStatus
            value2 = machine.oKOLDate
        }
        Constants.CONST_USERTYPE_TESTING -> {
            value1 = machine.testingStatus
            value2 = machine.testingDate
        }
        Constants.CONST_USERTYPE_FINISHING -> {
            value1 = machine.finishStatus
            value2 = machine.finishDate
        }
        Constants.CONST_USERTYPE_PDI_DOMESTIC, Constants.CONST_USERTYPE_PDI_EXPORT, Constants.CONST_USERTYPE_PDI_GT -> {
            value1 = machine.pdiStatus
            value2 = machine.pdiDate
        }
    }
    machine.rework.filter { it.reworkFrom == userTypePDI }.forEach {
        //Create row based on row index
        val row = sheet.createRow(index)
//Add data to each cell
        if (it.status == Constants.CONST_NOT_OK) createCell(
            row,
            3,
            "${it.description} - ${it.shortageReason}",
            IndexedColors.RED,
            workbook
        )
        else createCell(
            row,
            3,
            "${it.description}",
            IndexedColors.GREEN,
            workbook
        ) //Column 3
        createCell(row, 0, machine.machineNo, workbook = workbook) //Column 2
        createCell(row, 2, value2, workbook = workbook) //Column 3
        if (value1 == Constants.CONST_NOT_OK)
            createCell(row, 1, value1, IndexedColors.RED, workbook)
        else
            createCell(row, 1, value1, IndexedColors.GREEN, workbook)
        index++
    }
    return index
}

private fun createCell(
    row: Row,
    columnIndex: Int,
    value: String?,
    status: IndexedColors = IndexedColors.BLACK,
    workbook: Workbook
) {
    val cell = row.createCell(columnIndex)
    cell?.setCellValue(value)
    cell.cellStyle = getOkRemarkStyle(workbook, status)
}


fun createExcel(workbook: Workbook, activity: Activity) {
    val TAG = "Util.Function"
    //Get App Director, APP_DIRECTORY_NAME is a string
    val appDirectory = activity.getExternalFilesDir("case_construction_sheet")
    appDirectory.toString().printLog(TAG)
    if (appDirectory == null) {
        "0 Make dir ".printLog(TAG)
    }
    //Check App Directory whether it exists or not, create if not.
    if (appDirectory != null && !appDirectory.exists()) {
        appDirectory.mkdirs()
        "1 Make dir ".printLog(TAG)
    }

    try {
        "2 Make dir ".printLog(TAG)
        //Create excel file with extension .xlsx
        val excelFile =
            File(appDirectory, SimpleDateFormat("yyyyMMdd_HHmmss").format(Date()) + ".xlsx")
        excelFile.absolutePath.printLog(TAG)

        //Write workbook to file using FileOutputStream

        val fileOut = FileOutputStream(excelFile)
        workbook.write(fileOut)
        fileOut.flush()
        fileOut.close()
        startFileShareIntent(excelFile.absolutePath, activity)
    } catch (e: FileNotFoundException) {
        e.printStackTrace()
    } catch (e: Exception) {
        e.printStackTrace()
    } catch (e: IOException) {
        e.printStackTrace()
    }
}

private fun startFileShareIntent(
    filePath: String,
    activity: Activity
) { // pass the file path where the actual file is located.
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
            activity, activity.packageName + ".fileprovider",
            File(filePath)
        )
        putExtra(Intent.EXTRA_STREAM, fileURI)
    }

    activity.startActivity(Intent.createChooser(shareIntent, null))
}


fun createQRCode(context: Context, text: String): Bitmap?{
    var bitmap: Bitmap? = null

    // below line is for getting
    // the windowmanager service.
    val manager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager?

    // initializing a variable for default display.

    // initializing a variable for default display.
    val display: Display = manager!!.defaultDisplay

    // creating a variable for point which
    // is to be displayed in QR Code.

    // creating a variable for point which
    // is to be displayed in QR Code.
    val point = Point()
    display.getSize(point)

    // getting width and
    // height of a point

    // getting width and
    // height of a point
    val width: Int = point.x
    val height: Int = point.y

    // generating dimension from width and height.

    // generating dimension from width and height.
    var dimen = if (width < height) width else height
    dimen = dimen * 3 / 4

    // setting this dimensions inside our qr code
    // encoder to generate our qr code.

    // setting this dimensions inside our qr code
    // encoder to generate our qr code.
    val qrgEncoder = QRGEncoder(text, null, QRGContents.Type.TEXT, dimen)
    try {
        // getting our qrcode in the form of bitmap.
        bitmap = qrgEncoder.encodeAsBitmap()
        return bitmap
        // the bitmap is set inside our image
        // view using .setimagebitmap method.

    } catch (e: WriterException) {
        // this method is called for
        // exception handling.
        e.toString().printLog("QR CODE Generator")
    }
    return bitmap
}
