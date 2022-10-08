package com.example.bmi_okolo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.bmi_okolo.domain.BmiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
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
        _bodyMassIndexValue.value = bmiRepository.calculateBodyMassIndex(height, weight)
    }

    fun calculatePonderalIndex(height: Int, weight: Int) {
        _ponderalIndex.value = bmiRepository.calculateAndReturnPonderalIndex(height, weight)
    }

    fun bodyMassIndexCategory(bmi: Double) {
        _bodyMassIndexCategory.value = bmiRepository.checkBmiCategoryByValue(bmi)
    }

    fun bodyMassResultText(bmi: Double, name: String) {
        _bodyMassResultText.value = bmiRepository.bmiResultText(bmi, name)
    }

}