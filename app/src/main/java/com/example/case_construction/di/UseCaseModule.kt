package com.example.case_construction.di

import com.example.case_construction.repositories.DataRepositories
import com.example.case_construction.usecase.DataUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@InstallIn(ActivityRetainedComponent::class)
@Module
class UseCaseModule {
    @Provides
    fun providesUseCase(dataRepositories: DataRepositories): DataUseCase {
        return DataUseCase(dataRepositories)
    }
}