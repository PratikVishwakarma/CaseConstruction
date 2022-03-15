package com.example.case_construction.repositories

import com.example.case_construction.network.ApiService
import com.example.case_construction.network.api_model.*
import javax.inject.Inject


class DataRepositories @Inject constructor(private val apiService: ApiService) {

    suspend fun getBaseURLDataRepo(): BaseURLAPIDTO {
        return apiService.getBaseURLData()
    }

    suspend fun loginWithUsernameAndPasswordRepo(
        username: String,
        password: String
    ): UserListAPIDTO {
        return apiService.loginWithUsernameAndPassword(username, password)
    }

    suspend fun getMachineByNoRepo(
        username: String,
        password: String,
        userType: String
    ): MachineListAPIDTO {
        return apiService.getMachineByNo(username, password, userType)
    }

    suspend fun getMachineByStatusAndDateRepo(
        username: String,
        password: String,
        userType: String,
        date: String
    ): MachineListAPIDTO {
        return apiService.getMachineByStatusAndDate(username, password, userType, date)
    }

    suspend fun updateAndAddMachineStatusByNoRepo(
        userId: String, machineNo: String, userType: String, status: String, reworkArray: String
    ): SuccessAPIDTO {
        return apiService.updateAndAddMachineStatusByNo(userId, machineNo, userType, status, reworkArray)
    }

    suspend fun updateReworkStatusByIdRepo(
        userId: String, reworkId: String, status: String
    ): SuccessAPIDTO {
        return apiService.updateReworkStatusById(userId, reworkId, status)
    }

}