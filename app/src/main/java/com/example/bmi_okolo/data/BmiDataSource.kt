package com.example.bmi_okolo.data

import android.content.Context
import com.example.bmi_okolo.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.math.pow

class BmiDataSource @Inject constructor(
    @ApplicationContext  private val context: Context
) {

    fun calculateBodyMassIndex(height: Int, weight: Int): Double {
        val doubleHeight = height.toDouble() / 100
        return weight.toDouble() / doubleHeight.pow(2)
    }

    fun calculatePonderalIndex(height: Int, weight: Int): String {
        val doubleHeight = height.toDouble() / 100
        val ponderalIndex = weight.toDouble() / doubleHeight.pow(3)
        val formatString = String.format("%.2f", ponderalIndex)
        return context.getString(R.string.ponderal_text,formatString)
    }
}
