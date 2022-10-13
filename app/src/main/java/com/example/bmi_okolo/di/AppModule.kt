package com.example.bmi_okolo.di

import com.example.bmi_okolo.domain.BmiRepository
import com.example.bmi_okolo.data.BmiRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideBmiRepository(): BmiRepository = BmiRepositoryImpl()
}