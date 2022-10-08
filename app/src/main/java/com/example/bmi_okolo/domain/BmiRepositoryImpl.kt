package com.example.bmi_okolo.domain

import kotlin.math.pow


class BmiRepositoryImpl : BmiRepository {

    /**
     * > The function takes two parameters, height and weight, and returns the body mass index
     */
    override fun calculateBodyMassIndex(height: Int, weight: Int): Double {
        val doubleHeight = height.toDouble() / 100

        return weight.toDouble() / (doubleHeight * doubleHeight)
    }

    override fun calculateAndReturnPonderalIndex(height: Int, weight: Int): String {
        val doubleHeight = height.toDouble() / 100
        val ponderalIndex = weight.toDouble() / doubleHeight.pow(3)
        val formatString = String.format("%.2f", ponderalIndex)
        return "Ponderal Index: ${formatString}kg/m3"
    }

    override fun checkBmiCategoryByValue(bmi: Double): String {
        when (bmi) {
            in 1.0..18.49 -> {
                return "Underweight BMI range: 1.0kg/m2 - 18.49kg/m2"
            }
            in 18.5..24.9 -> {
                return "Normal BMI range: 18.5kg/m2 - 25kg/m2"
            }
            in 25.0..29.9 -> {
                return "Overweight BMI range: 25.0kg/m2 - 29.9kg/m2"
            }
        }
        return "Obesity BMI range: 30.0kg/m2 - higher"
    }

    override fun bmiResultText(bmi: Double, name: String): String {
        when (bmi) {
            in 1.0..18.49 -> {
                return "Hello ${name}, You are Underweight"
            }
            in 18.5..24.9 -> {
                return "Hello ${name}, You are Normal"
            }
            in 25.0..29.9 -> {
                return "Hello ${name}, You are Overweight"
            }
        }
        return "Hello ${name}, You are Obese"
    }


}
