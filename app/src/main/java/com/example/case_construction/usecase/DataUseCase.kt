package com.example.case_construction.usecase

import com.example.case_construction.model.ResultData
import com.example.case_construction.network.NetworkConstants
import com.example.case_construction.network.api_model.BaseURLAPIDTO
import com.example.case_construction.network.api_model.UserListAPIDTO
import com.example.case_construction.repositories.DataRepositories
import javax.inject.Inject

class DataUseCase @Inject constructor(private val dataRepositories: DataRepositories) {

    suspend fun getBaseURLDataRepo(): ResultData<BaseURLAPIDTO> {
        return try {
            val data = dataRepositories.getBaseURLDataRepo()
            when (data.status) {
                NetworkConstants.INT_STATUS_SUCCESS -> ResultData.Success(data)
                NetworkConstants.INT_STATUS_NO_DATA_AVAILABLE -> {
                    ResultData.NoContent(data.message)
                }
                else -> ResultData.Failed("Something went wrong. Please try again!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResultData.Failed("Something went wrong. Please try again!")
        }
    }

    suspend fun loginWithUsernameAndPasswordRepo(username: String, password: String): ResultData<UserListAPIDTO> {
        return try {
            val data = dataRepositories.loginWithUsernameAndPasswordRepo(username, password)
            when (data.status) {
                NetworkConstants.INT_STATUS_SUCCESS -> ResultData.Success(data)
                NetworkConstants.INT_STATUS_NO_DATA_AVAILABLE -> {
                    ResultData.NoContent(data.message)
                }
                else -> ResultData.Failed("Something went wrong. Please try again!")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            ResultData.Failed("Something went wrong. Please try again!")
        }
    }

}