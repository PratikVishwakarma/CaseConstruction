package com.example.case_construction.network

import com.example.case_construction.network.api_model.*
import retrofit2.http.*

interface ApiService {

    @GET(NetworkConstants.URL_ALL_REQUIRED_DATA)
    suspend fun getBaseURLData(): BaseURLAPIDTO

}