package com.example.bmi_okolo.domain

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculatePonderalIndexUseCase @Inject constructor(
    private val bmiRepository: BmiRepository,
) {

    suspend operator fun invoke(height: Int, weight: Int): String =
        withContext(Dispatchers.Default) {
            val ponderalIndex = bmiRepository.calculatePonderalIndex(height, weight)
            ponderalIndex
        }
}