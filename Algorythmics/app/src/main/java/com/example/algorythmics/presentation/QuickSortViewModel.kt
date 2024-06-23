package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuickSortViewModel : ViewModel() {

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> get() = _comparisonMessage

    private var currentSortingJob: Job? = null

    private var originalList = mutableListOf<ListUiItem>()

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
        originalList = list
    }

    fun startQuickSorting() {
        currentSortingJob?.cancel()

        currentSortingJob = viewModelScope.launch {
            val list = _listToSort.value!!.toMutableList()

            quickSort(list, 0, list.size - 1)

            for (i in list.indices) {
                list[i] = list[i].copy(isSorted = true, isCurrentlyCompared = false)
            }

            _listToSort.value = list.toList()

            val completionMessage = "Gyorsrendezés befejezve."
            _comparisonMessage.postValue(completionMessage)
        }
    }

    private suspend fun quickSort(list: MutableList<ListUiItem>, low: Int, high: Int) {
        if (low < high) {

            val partitionIndex = partition(list, low, high)

            quickSort(list, low, partitionIndex - 1)
            quickSort(list, partitionIndex + 1, high)
        }
    }

    private suspend fun partition(list: MutableList<ListUiItem>, low: Int, high: Int): Int {
        val pivot = list[high].value
        var i = low - 1

        for (j in low until high) {
            // Mark elements being compared (red color)
            list[j] = list[j].copy(isCurrentlyCompared = true)
            list[high] = list[high].copy(isCurrentlyCompared = true)
            _listToSort.value = list.toList()

            val comparisonMessage = "Elemek összehasonlítva: ${list[j].value} és $pivot"
            _comparisonMessage.postValue(comparisonMessage)

            delay(800)

            if (list[j].value < pivot) {
                i++
                // Swap elements
                val temp = list[i]
                list[i] = list[j]
                list[j] = temp

                val swapMessage = "Elemek felcserélve: ${list[i].value} és ${list[j].value}"
                _comparisonMessage.postValue(swapMessage)

                _listToSort.value = list.toList()
                delay(800)
            }

            list[j] = list[j].copy(isCurrentlyCompared = false)
            list[high] = list[high].copy(isCurrentlyCompared = false)
            _listToSort.value = list.toList()
            delay(800)
        }

        val temp = list[i + 1]
        list[i + 1] = list[high]
        list[high] = temp

        val pivotSwapMessage = "Pivot (${list[i + 1].value}) helyére rendezve."
        _comparisonMessage.postValue(pivotSwapMessage)

        list[i + 1] = list[i + 1].copy(isSorted = true)
        _listToSort.value = list.toList()
        delay(800)

        return i + 1
    }



    fun shuffleList() {
        val list = _listToSort.value ?: return
        val shuffledList = list.shuffled()
        _listToSort.value = shuffledList.toMutableList()
    }

    var stack = mutableListOf<Pair<Int, Int>>()
    var currentLow = 0
    var currentHigh = 0
    var pivotIndex = 0
    var i = 0
    var j = 0
    var stepState = StepState.START

    enum class StepState {
        START, PARTITION, COMPARE, SWAP, FINAL_SWAP, COMPLETE
    }

    fun stepQuickSorting() {
        viewModelScope.launch {
            val list = _listToSort.value!!.toMutableList()

            if (stack.isEmpty() && stepState == StepState.START) {
                stack.add(Pair(0, list.size - 1))
                stepState = StepState.PARTITION
            }

            if (stack.isNotEmpty()) {
                val (low, high) = stack.last()
                currentLow = low
                currentHigh = high

                when (stepState) {
                    StepState.PARTITION -> {
                        i = low - 1
                        j = low
                        pivotIndex = high
                        stepState = StepState.COMPARE
                    }

                    StepState.COMPARE -> {
                        if (j < high) {
                            // Mark elements being compared (red color)
                            list[j] = list[j].copy(isCurrentlyCompared = true)
                            list[pivotIndex] = list[pivotIndex].copy(isCurrentlyCompared = true)
                            _listToSort.value = list.toList()

                            if (list[j].value < list[pivotIndex].value) {
                                i++
                                stepState = StepState.SWAP
                            } else {
                                // Reset comparison status if no swap
                                list[j] = list[j].copy(isCurrentlyCompared = false)
                                list[pivotIndex] = list[pivotIndex].copy(isCurrentlyCompared = false)
                                _listToSort.value = list.toList()
                                delay(800)
                                j++
                            }
                        } else {
                            stepState = StepState.FINAL_SWAP
                        }
                    }

                    StepState.SWAP -> {
                        // Swap elements
                        val temp = list[i]
                        list[i] = list[j]
                        list[j] = temp

                        // Update list after swap
                        _listToSort.value = list.toList()
                        delay(800)
                        stepState = StepState.COMPARE
                        j++
                    }

                    StepState.FINAL_SWAP -> {
                        // Swap pivot element with element at i+1
                        val temp = list[i + 1]
                        list[i + 1] = list[pivotIndex]
                        list[pivotIndex] = temp

                        // Mark pivot as sorted (gray color)
                        list[i + 1] = list[i + 1].copy(isSorted = true)
                        _listToSort.value = list.toList()

                        stack.removeAt(stack.lastIndex)
                        if (i + 1 < high) stack.add(Pair(i + 2, high))
                        if (low < i) stack.add(Pair(low, i))
                        stepState = if (stack.isEmpty()) StepState.COMPLETE else StepState.PARTITION
                    }

                    StepState.COMPLETE -> {
                        for (k in list.indices) {
                            list[k] = list[k].copy(isSorted = true, isCurrentlyCompared = false)
                        }
                        _listToSort.value = list.toList()
                    }

                    else -> {}
                }

                // Update the list to reflect the comparison status
                for (k in list.indices) {
                    if (k != pivotIndex && k != j && k != i) {
                        list[k] = list[k].copy(isCurrentlyCompared = false)
                    }
                }
                _listToSort.value = list.toList()
            }
        }
    }

    fun restartQuickSort() {
        viewModelScope.launch {
            currentSortingJob?.cancel()


            _animationSteps.value = ""

            _listToSort.value = originalList.map { it.copy(isSorted = false, isCurrentlyCompared = false, color = Color.Transparent) }

            startQuickSorting()
        }
    }
}