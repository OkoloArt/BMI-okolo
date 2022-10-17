package com.example.bmi_okolo.data

import android.content.Context
import com.example.bmi_okolo.R
import com.example.bmi_okolo.domain.BmiRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.pow


@Singleton
class BmiRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val bmiDataSource: BmiDataSource
) : BmiRepository {

    /**
     * > The function takes two parameters, height and weight, and returns the body mass index
     */
    override fun calculateBodyMassIndex(height: Int, weight: Int): Double {
       return bmiDataSource.calculateBodyMassIndex(height, weight)
    }

    override fun calculatePonderalIndex(height: Int, weight: Int): String {
      return bmiDataSource.calculatePonderalIndex(height, weight)
    }
}
