package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.presentation.ListUiItem
import com.example.algorythmics.use_case.BubbleSortUseCase
import kotlinx.coroutines.launch
import kotlin.random.Random


class SortViewModel(private val bubbleSortUseCase: BubbleSortUseCase = BubbleSortUseCase()) : ViewModel() {

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> = _comparisonMessage

    fun updateComparisonMessage(message: String) {
        _comparisonMessage.value = message
    }

    init {
        val list = mutableListOf<ListUiItem>()
        for (i in 0 until 10) {
            list.add(
                ListUiItem(
                    id = i,
                    isCurrentlyCompared = false,
                    value = Random.nextInt(150)
                )
            )
        }
        _listToSort.value = list
    }

    fun startSorting() {
        viewModelScope.launch {
            bubbleSortUseCase(_listToSort.value!!.map { it.value }.toMutableList()).collect { swapInfo ->
                val currentItemIndex = swapInfo.currentItem
                val newList = _listToSort.value!!.toMutableList()
                newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = true)
                newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = true)

                if (swapInfo.shouldSwap) {
                    val firstItem = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex + 1] = firstItem
                    _comparisonMessage.postValue("Elemek felcserélve: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}")
                }
                if (swapInfo.hadNoEffect) {
                    newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                    _comparisonMessage.postValue("Elemek nem cseréltek helyet: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}")
                }
                _listToSort.value = newList

                val step = "Step: $currentItemIndex, Comparison: ${swapInfo.currentItem}, Should Swap: ${swapInfo.shouldSwap}, Had No Effect: ${swapInfo.hadNoEffect}"
                _animationSteps.postValue(step)
            }
        }
    }

}