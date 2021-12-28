package com.example.case_construction.ui

import androidx.lifecycle.ViewModel
import com.example.case_construction.usecase.DataUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val dataUseCase: DataUseCase) :
    ViewModel() {

//    fun getRepositoriesData(): LiveData<ResultData<GithubDataModel>> {
//        return liveData<ResultData<GithubDataModel>> {
//            emit(ResultData.Loading())
//            emit(dataUseCase.getDataRepositories())
//        }
//    }
}