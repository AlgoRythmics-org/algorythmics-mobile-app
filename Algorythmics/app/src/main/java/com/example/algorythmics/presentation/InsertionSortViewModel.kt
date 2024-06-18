package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.animation.SortingViewModel
import com.example.algorythmics.use_case.InsertionSortUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class InsertionSortViewModel(private val insertionSortUseCase: InsertionSortUseCase = InsertionSortUseCase())
    : SortingViewModel() {

    private val _originalList = MutableLiveData<List<ListUiItem>>()
    val originalList: LiveData<List<ListUiItem>> get() = _originalList

    private val _sortedList = MutableLiveData<List<ListUiItem>>()
    val sortedList: LiveData<List<ListUiItem>> get() = _sortedList

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> get() = _comparisonMessage

    private var currentStep = 0
    private var isSorting = false
    private var phase = 0
    private var comparisonIndex = 0


    init {
        val list = mutableListOf<ListUiItem>()
        for ((i, item) in insertionSortUseCase.items.withIndex()) {
            list.add(
                ListUiItem(
                    id = i,
                    value = item,
                )
            )
        }
        _originalList.value = list.toList()
        _sortedList.value = list.toList()
    }

    fun startInsertionSorting() {
        viewModelScope.launch {
            _sortedList.value?.let { initialList ->
                val newList = initialList.toMutableList()
                newList[0] = newList[0].copy(isSorted = true)
                _sortedList.value = newList.toList()
                delay(1000)

                for (i in 1 until newList.size) {
                    var j = i
                    _sortedList.value = newList.toList()
                    delay(1500)

                    newList[j] = newList[j].copy(isCurrentlyCompared = true)
                    _sortedList.value = newList.toList()
                    delay(800)

                    while (j > 0 && newList[j].value < newList[j - 1].value) {
                        val comparisonMessage = "Összehasonlítva: ${newList[j].value} és ${newList[j - 1].value}"
                        _comparisonMessage.postValue("$comparisonMessage\nElemek felcserélve: ${newList[j].value} és ${newList[j - 1].value}")

                        val temp = newList[j]
                        newList[j] = newList[j - 1]
                        newList[j - 1] = temp

                        _sortedList.value = newList.toList()
                        delay(800)

                        j--
                    }

                    if (j > 0 && newList[j].value >= newList[j - 1].value) {
                        val comparisonMessage = "Összehasonlítva: ${newList[j].value} és ${newList[j - 1].value}"
                        _comparisonMessage.postValue("$comparisonMessage\nElemek nem cseréltek helyet: ${newList[j].value} és ${newList[j - 1].value}")
                    }

                    newList[j] = newList[j].copy(isCurrentlyCompared = false, isSorted = true)
                    _sortedList.value = newList.toList()
                    val sortedMessage = "Rendezett elem: ${newList[j].value} "
                    _comparisonMessage.postValue(sortedMessage)
                    delay(800)

                    val step = "Index: $i, Inner Loop Index: $j, Should Swap: ${j > 0 && newList[j].value < newList[j - 1].value}, Sorted Up to Index: $j"
                    _animationSteps.postValue(step)
                }

                _sortedList.value = newList.map { it.copy(isSorted = true) }
            }
        }
    }




    fun stepInsertionSort() {
        viewModelScope.launch {
            val currentListSize = _sortedList.value?.size ?: return@launch
            if (!isSorting) {
                isSorting = true
                currentStep = 1
                phase = 0
                comparisonIndex = 1
            }

            val newList = _sortedList.value?.toMutableList() ?: return@launch

            when {
                phase == 0 -> {
                    newList[0] = newList[0].copy(isSorted = true, color = Color.Gray)
                    _sortedList.value = newList.toList()
                    phase = 1
                }
                phase == 1 -> {
                    if (currentStep < currentListSize) {
                        newList[currentStep] = newList[currentStep].copy(isCurrentlyCompared = true, color = Color.Red)
                        _sortedList.value = newList.toList()
                        delay(1000)
                        phase = 2
                    } else {
                        isSorting = false
                        for (i in 0 until currentListSize) {
                            if (!newList[i].isSorted) {
                                newList[i] = newList[i].copy(isCurrentlyCompared = false, isSorted = true, color = Color.Gray)
                            }
                        }
                        _sortedList.value = newList.toList()
                    }
                }
                phase == 2 -> {
                    if (comparisonIndex > 0 && newList[comparisonIndex].value < newList[comparisonIndex - 1].value) {
                        val temp = newList[comparisonIndex]
                        newList[comparisonIndex] = newList[comparisonIndex - 1]
                        newList[comparisonIndex - 1] = temp

                        newList[comparisonIndex] = newList[comparisonIndex].copy(color = Color.Red)
                        newList[comparisonIndex - 1] = newList[comparisonIndex - 1].copy(color = Color.Gray)
                        _sortedList.value = newList.toList()

                        comparisonIndex--
                    } else {
                        newList[comparisonIndex] = newList[comparisonIndex].copy(isCurrentlyCompared = false, isSorted = true, color = Color.Gray)
                        _sortedList.value = newList.toList()

                        phase = 1
                        currentStep++
                        comparisonIndex = currentStep
                    }
                }
            }
        }
    }

    fun shuffleList() {
        val list = _originalList.value ?: return
        val shuffledList = list.shuffled()
        _sortedList.value = shuffledList
    }
}