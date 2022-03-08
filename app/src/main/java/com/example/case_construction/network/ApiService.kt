package com.example.case_construction.network

import com.example.case_construction.network.api_model.*
import org.apache.xmlbeans.UserType
import retrofit2.http.*

interface ApiService {

    @GET(NetworkConstants.URL_ALL_REQUIRED_DATA)
    suspend fun getBaseURLData(): BaseURLAPIDTO

    @GET(NetworkConstants.URL_LOGIN)
    suspend fun loginWithUsernameAndPassword(
        @Query("username") username: String,
        @Query("password") password: String
    ): UserListAPIDTO

    @GET(NetworkConstants.URL_GET_MACHINE_BY_NO)
    suspend fun getMachineByNo(
        @Query("userid") userId: String,
        @Query("machineNo") machineNo: String,
        @Query("userType") userType: String
    ): MachineListAPIDTO

    @GET(NetworkConstants.URL_GET_MACHINE_BY_NO)
    suspend fun updateAndAddMachineStatusByNo(
        @Query("userid") userId: String,
        @Query("machineNo") machineNo: String,
        @Query("userType") userType: String
    ): SuccessAPIDTO

}