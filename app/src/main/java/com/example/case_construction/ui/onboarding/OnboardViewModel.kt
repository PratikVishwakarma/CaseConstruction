package com.example.case_construction.ui.onboarding

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.case_construction.model.ResultData
import com.example.case_construction.network.api_model.*
import com.example.case_construction.usecase.DataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OnboardViewModel @Inject constructor(private val dataUseCase: DataUseCase) :
    ViewModel() {

    fun getBaseURLDataVM(): LiveData<ResultData<BaseURLAPIDTO>> {
        return liveData<ResultData<BaseURLAPIDTO>> {
            emit(ResultData.Loading())
            emit(dataUseCase.getBaseURLDataRepo())
        }
    }
}