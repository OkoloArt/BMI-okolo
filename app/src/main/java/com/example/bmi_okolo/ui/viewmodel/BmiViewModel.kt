package com.example.bmi_okolo.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bmi_okolo.domain.CalculateBmiUseCase
import com.example.bmi_okolo.domain.CalculatePonderalIndexUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BmiViewModel @Inject constructor(
    private val calculateBmiUseCase: CalculateBmiUseCase,
    private val calculatePonderalIndexUseCase: CalculatePonderalIndexUseCase
) : ViewModel() {

    var bodyMassIndex = 0.0
    var ponderalIndex = ""

    private var _userName: String = ""
    val userName get() = _userName

    fun calculateBodyMassIndex(height: Int, weight: Int) {
        viewModelScope.launch {
            bodyMassIndex = calculateBmiUseCase(height, weight)
        }
    }

    fun calculatePonderalIndex(height: Int, weight: Int) {
        viewModelScope.launch {
            ponderalIndex = calculatePonderalIndexUseCase(height, weight)
        }
    }

    fun setName(name: String) {
        _userName = name
    }
}