package com.example.case_construction.repositories

import com.example.case_construction.network.ApiService
import com.example.case_construction.network.api_model.*
import javax.inject.Inject


class DataRepositories @Inject constructor(private val apiService: ApiService) {

    suspend fun getBaseURLDataRepo(): BaseURLAPIDTO {
        return apiService.getBaseURLData()
    }
    suspend fun loginWithUsernameAndPasswordRepo(username: String, password: String): UserListAPIDTO {
        return apiService.loginWithUsernameAndPassword(username, password)
    }

}