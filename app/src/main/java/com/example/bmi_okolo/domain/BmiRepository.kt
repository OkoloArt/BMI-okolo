package com.example.bmi_okolo.domain

interface BmiRepository {

    fun calculateBodyMassIndex(height: Int, weight: Int): Double

    fun calculatePonderalIndex(height: Int, weight: Int): String
}