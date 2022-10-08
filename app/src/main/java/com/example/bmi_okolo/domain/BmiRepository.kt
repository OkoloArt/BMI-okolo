package com.example.bmi_okolo.domain

interface BmiRepository {

    fun calculateBodyMassIndex(height: Int, weight: Int): Double

    fun calculateAndReturnPonderalIndex(height: Int, weight: Int): String

    fun checkBmiCategoryByValue(bmi: Double): String

    fun bmiResultText(bmi: Double, name:String): String
}