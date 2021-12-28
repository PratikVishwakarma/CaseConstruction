package com.example.case_construction.di

import com.example.case_construction.network.ApiService
import com.example.case_construction.repositories.DataRepositories
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class RepositoriesModule {
    @Provides
    fun providesDataRepository(apiService: ApiService): DataRepositories {
        return DataRepositories(apiService)
    }
}