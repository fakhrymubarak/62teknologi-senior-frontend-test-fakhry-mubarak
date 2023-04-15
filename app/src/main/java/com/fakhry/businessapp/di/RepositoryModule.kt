package com.fakhry.businessapp.di

import com.fakhry.businessapp.data.business.repository.BusinessRepositoryImpl
import com.fakhry.businessapp.domain.business.repository.BusinessRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [NetworkModule::class])
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun provideBusinessRepository(businessRepository: BusinessRepositoryImpl): BusinessRepository

}