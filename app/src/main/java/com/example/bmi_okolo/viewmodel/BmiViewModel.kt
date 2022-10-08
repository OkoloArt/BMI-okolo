package com.example.bmi_okolo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bmi_okolo.domain.BmiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BmiViewModel @Inject constructor(
    private val bmiRepository: BmiRepository,
) : ViewModel() {

    private val _bodyMassIndexValue = MutableLiveData<Double>()
    val bodyMassIndexValue: LiveData<Double> get() = _bodyMassIndexValue

    private val _ponderalIndex = MutableLiveData<String>()
    val ponderalIndex: LiveData<String> get() = _ponderalIndex

    private val _bodyMassIndexCategory = MutableLiveData<String>()
    val bodyMassIndexCategory: LiveData<String> get() = _bodyMassIndexCategory

    private val _bodyMassResultText = MutableLiveData<String>()
    val bodyMassResultText: LiveData<String> get() = _bodyMassResultText

    fun calculateBodyMassIndex(height: Int, weight: Int) {
        viewModelScope.launch {
            _bodyMassIndexValue.value = bmiRepository.calculateBodyMassIndex(height, weight)
        }
    }

    fun calculatePonderalIndex(height: Int, weight: Int) {
        viewModelScope.launch {
            _ponderalIndex.value = bmiRepository.calculateAndReturnPonderalIndex(height, weight)
        }
    }

    fun bodyMassIndexCategory(bmi: Double) {
        viewModelScope.launch {
            _bodyMassIndexCategory.value = bmiRepository.checkBmiCategoryByValue(bmi)
        }
    }

    fun bodyMassResultText(bmi: Double, name: String) {
        viewModelScope.launch {
            _bodyMassResultText.value = bmiRepository.bmiResultText(bmi, name)
        }
    }

}