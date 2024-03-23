package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.fragments.course.SortedListAdapter
import com.example.algorythmics.use_case.BubbleSortUseCase
import kotlinx.coroutines.launch
import kotlin.random.Random


class SortViewModel(
    private val bubbleSortUseCase: BubbleSortUseCase = BubbleSortUseCase(),
) : ViewModel() {

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> = _comparisonMessage

    private var isSortingInProgress = false
    private var currentStep = 0

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
                    val comparisonMessage = "Összehasonlítva: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}"
                    _comparisonMessage.postValue("$comparisonMessage\nElemek felcserélve: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}")
                } else if (swapInfo.hadNoEffect) {
                    newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                    val comparisonMessage = "Összehasonlítva: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}"
                    _comparisonMessage.postValue("$comparisonMessage\nElemek nem cseréltek helyet: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}")
                }



                _listToSort.value = newList

                val step = "Step: $currentItemIndex, Comparison: ${swapInfo.currentItem}, Should Swap: ${swapInfo.shouldSwap}, Had No Effect: ${swapInfo.hadNoEffect}"
                _animationSteps.postValue(step)
            }
        }
    }



    fun stepSorting() {
        viewModelScope.launch {
            performNextStep() // Egy lépést hajt végre minden híváskor

        }
    }

    private fun isListSorted(): Boolean {
        val sortedList = _listToSort.value?.map { it.value }?.sorted()
        val currentList = _listToSort.value?.map { it.value }
        return sortedList == currentList
    }

    private var alreadySorted = 0 // Ez a változó nyomon követi, hány elem van már a helyén

    private fun performNextStep() {
        if (!isListSorted()) {
            if (currentStep < _listToSort.value!!.size - 1 - alreadySorted) {
                val listToSort = _listToSort.value!!.map { it.value }.toMutableList()
                val swapInfo = bubbleSortUseCase.performNextStep(listToSort, currentStep)
                val newList = _listToSort.value!!.toMutableList()

                // Összehasonlított elemek színének beállítása
                val firstComparedIndex = currentStep
                val secondComparedIndex = currentStep + 1
                newList[firstComparedIndex] = newList[firstComparedIndex].copy(isCurrentlyCompared = true)
                newList[secondComparedIndex] = newList[secondComparedIndex].copy(isCurrentlyCompared = true)

                val comparisonMessage = "Összehasonlítva: ${newList[firstComparedIndex].value} és ${newList[secondComparedIndex].value}"
                _comparisonMessage.postValue(comparisonMessage)

                if (swapInfo.shouldSwap) {
                    val firstItem = newList[currentStep].copy(isCurrentlyCompared = false)
                    newList[currentStep] = newList[currentStep + 1].copy(isCurrentlyCompared = false)
                    newList[currentStep + 1] = firstItem
                    _comparisonMessage.postValue("$comparisonMessage\nElemek felcserélve: ${newList[currentStep].value} és ${newList[currentStep + 1].value}")
                }
                if (swapInfo.hadNoEffect) {
                    newList[currentStep] = newList[currentStep].copy(isCurrentlyCompared = false)
                    newList[currentStep + 1] = newList[currentStep + 1].copy(isCurrentlyCompared = false)
                    _comparisonMessage.postValue("$comparisonMessage\nElemek nem cseréltek helyet: ${newList[currentStep].value} és ${newList[currentStep + 1].value}")
                }
                _listToSort.postValue(newList)

                val step = "Step: $currentStep, Comparison: ${swapInfo.currentItem}, Should Swap: ${swapInfo.shouldSwap}, Had No Effect: ${swapInfo.hadNoEffect}"
                _animationSteps.postValue(step)

                // Összehasonlított elemek színének visszaállítása
                newList[firstComparedIndex] = newList[firstComparedIndex].copy(isCurrentlyCompared = false)
                newList[secondComparedIndex] = newList[secondComparedIndex].copy(isCurrentlyCompared = false)

                currentStep++
            } else {
                currentStep = 0
                isSortingInProgress = false
                alreadySorted++
            }
        }
    }


}
