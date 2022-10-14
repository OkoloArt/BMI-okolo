package com.example.bmi_okolo.domain

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculateBmiUseCase @Inject constructor(
    private val bmiRepository: BmiRepository,
) {

    suspend operator fun invoke(height: Int, weight: Int): Double =
        withContext(Dispatchers.Default) {
            val bodyMassIndex = bmiRepository.calculateBodyMassIndex(height, weight)
            bodyMassIndex
        }
}