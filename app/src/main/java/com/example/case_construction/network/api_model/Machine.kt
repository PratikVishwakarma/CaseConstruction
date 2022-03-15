package com.example.case_construction.network.api_model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class Machine : Serializable {
    @SerializedName("Id")
    var id = ""

    @SerializedName("daysPlan")
    var daysPlan = ""

    @SerializedName("machineNo")
    var machineNo = ""

    @SerializedName("lineUpDate")
    var lineUpDate = ""

    @SerializedName("modelNo")
    var modelNo = ""

    @SerializedName("chassis")
    var chassis = ""

    @SerializedName("drive")
    var drive = ""

    @SerializedName("ldrValve")
    var ldrValve = ""

    @SerializedName("rtd")
    var rtd = ""

    @SerializedName("hammer")
    var hammer = ""

    @SerializedName("bhValve")
    var bhValve = ""

    @SerializedName("bhPattern")
    var bhPattern = ""

    @SerializedName("engine")
    var engine = ""

    @SerializedName("counterWeight")
    var counterWeight = ""

    @SerializedName("loaderArm")
    var loaderArm = ""

    @SerializedName("cabin")
    var cabin = ""

    @SerializedName("beacon")
    var beacon = ""

    @SerializedName("fender")
    var fender = ""

    @SerializedName("dipper")
    var dipper = ""

    @SerializedName("bhBucket")
    var bhBucket = ""

    @SerializedName("ldrBucket")
    var ldrBucket = ""

    @SerializedName("tyreFront")
    var tyreFront = ""

    @SerializedName("tyreRear")
    var tyreRear = ""

    @SerializedName("lenguage")
    var lenguage = ""

    @SerializedName("market")
    var market = ""

    @SerializedName("month")
    var month = ""

    @SerializedName("lineupNumber")
    var lineupNumber = ""

    @SerializedName("oKOLStatus")
    var oKOLStatus = ""

    @SerializedName("stage")
    var stage = ""

    @SerializedName("oKOLDate")
    var oKOLDate = ""

    @SerializedName("oKOLUserid")
    var oKOLUserid = ""

    @SerializedName("testingStatus")
    var testingStatus = ""

    @SerializedName("testingDate")
    var testingDate = ""

    @SerializedName("testingUserid")
    var testingUserid = ""

    @SerializedName("finishStatus")
    var finishStatus = ""

    @SerializedName("finishDate")
    var finishDate = ""

    @SerializedName("finishuserid")
    var finishuserid = ""

    @SerializedName("pdiStatus")
    var pdiStatus = ""

    @SerializedName("pdiDate")
    var pdiDate = ""

    @SerializedName("telematics")
    var telematics = ""

    @SerializedName("rework")
    var rework : ArrayList<Rework> = ArrayList()
}

