package com.example.bmi_okolo.di

import com.example.bmi_okolo.data.BmiRepositoryImpl
import com.example.bmi_okolo.domain.BmiRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun provideBmiRepository(bmiRepositoryImpl: BmiRepositoryImpl): BmiRepository
}