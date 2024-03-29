package com.example.case_construction.ui.machine

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.case_construction.model.ResultData
import com.example.case_construction.network.api_model.*
import com.example.case_construction.usecase.DataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MachineViewModel @Inject constructor(private val dataUseCase: DataUseCase) :
    ViewModel() {


    fun getMachineByNoVM(userId: String, machineNo: String, userType: String): LiveData<ResultData<MachineListAPIDTO>> {
        return liveData<ResultData<MachineListAPIDTO>> {
            emit(ResultData.Loading())
            emit(dataUseCase.getMachineByNoRepo(userId, machineNo, userType))
        }
    }

    fun getMachineByStatusAndDate(userId: String, machineNo: String, userType: String, date: String): LiveData<ResultData<MachineListAPIDTO>> {
        return liveData<ResultData<MachineListAPIDTO>> {
            emit(ResultData.Loading())
            emit(dataUseCase.getMachineByStatusAndDateUseCase(userId, machineNo, userType, date))
        }
    }

    fun updateAndAddMachineStatusByNoVM(userId: String, machineNo: String, userType: String, status: String, reworkArray: String, isNotUpdateStage: String = "0"): LiveData<ResultData<SuccessAPIDTO>> {
        return liveData<ResultData<SuccessAPIDTO>> {
            emit(ResultData.Loading())
            emit(dataUseCase.updateAndAddMachineStatusByNoUseCase(userId, machineNo, userType, status, reworkArray, isNotUpdateStage))
        }
    }

    fun updateReworkStatusByIdVM(userId: String, reworkId: String, status: String, machineNo: String = ""): LiveData<ResultData<SuccessAPIDTO>> {
        return liveData<ResultData<SuccessAPIDTO>> {
            emit(ResultData.Loading())
            emit(dataUseCase.updateReworkStatusByIdUseCase(userId, reworkId, status, machineNo))
        }
    }

}