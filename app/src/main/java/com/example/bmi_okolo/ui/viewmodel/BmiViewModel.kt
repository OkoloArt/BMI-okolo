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

//    private val _bodyMassIndexValue = MutableLiveData<Double>()
//    val bodyMassIndexValue: LiveData<Double> get() = _bodyMassIndexValue

    var bodyMassIndex = 0.0
    var ponderalIndex = ""

//    private val _ponderalIndex = MutableLiveData<String>()
//    val ponderalIndex: LiveData<String> get() = _ponderalIndex
//
//    private val _bodyMassIndexCategory = MutableLiveData<String>()
//    val bodyMassIndexCategory: LiveData<String> get() = _bodyMassIndexCategory
//
//    private val _bodyMassResultText = MutableLiveData<String>()
//    val bodyMassResultText: LiveData<String> get() = _bodyMassResultText

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
}