package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.ShellSortUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class ShellSortViewModel(private val shellSortUseCase: ShellSortUseCase = ShellSortUseCase()) : ViewModel() {

    private var originalList = mutableListOf<ListUiItem>()
    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> get() = _comparisonMessage

    private var currentSortingJob: Job? = null

    private var gap = 0
    private var currentComparisonIndex = 0

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
        gap = list.size / 2
        originalList = list
    }

    fun startShellSorting() {
        currentSortingJob?.cancel()
        currentSortingJob =  viewModelScope.launch {
            shellSortUseCase.sort(_listToSort.value!!.map { it.value }.toMutableList())
                .collect { swapInfo ->
                    val currentItemIndex = swapInfo.currentItem

                    if (currentItemIndex >= 0 && currentItemIndex < _listToSort.value!!.size) {
                        val newList = _listToSort.value!!.toMutableList()

                        val itemToCompareIndex = swapInfo.itemToSwap
                        if (itemToCompareIndex != -1) {

                            val temp = newList[currentItemIndex]
                            newList[currentItemIndex] = newList[itemToCompareIndex]
                            newList[itemToCompareIndex] = temp

                            val comparisonMessage = "Összehasonlítva: ${newList[currentItemIndex].value} és ${newList[itemToCompareIndex].value}"
                            _comparisonMessage.postValue("$comparisonMessage\nElemek felcserélve: ${newList[currentItemIndex].value} és ${newList[itemToCompareIndex].value}")

                            newList[currentItemIndex] =
                                newList[currentItemIndex].copy(isCurrentlyCompared = true)
                            newList[itemToCompareIndex] =
                                newList[itemToCompareIndex].copy(isCurrentlyCompared = true)


                            _listToSort.value = newList.toList()

                            delay(800)


                            newList[currentItemIndex] =
                                newList[currentItemIndex].copy(isCurrentlyCompared = false)
                            newList[itemToCompareIndex] =
                                newList[itemToCompareIndex].copy(isCurrentlyCompared = false)
                        } else {
                            // Reset all isCurrentlyCompared flags
                            newList.forEachIndexed { index, listUiItem ->
                                newList[index] = listUiItem.copy(isCurrentlyCompared = false)
                            }


                            val noSwapMessage = "Összehasonlítva: ${newList[currentItemIndex].value} és nincs csere"
                            _comparisonMessage.postValue(noSwapMessage)


                            _listToSort.value = newList.toList()
                        }

                        val stepMessage = "Index: $currentItemIndex, Elemek: ${newList[currentItemIndex].value}, Cserélhető Elem: $itemToCompareIndex"
                        _animationSteps.postValue(stepMessage)
                    }
                }

            val finalSortedList = _listToSort.value!!.map { it.copy(isSorted = true) }
            _listToSort.postValue(finalSortedList.toList())
        }
    }

    fun shuffleList() {
        val list = _listToSort.value ?: return
        val shuffledList = list.shuffled()
        _listToSort.value = shuffledList.toMutableList()
    }

    fun stepShellSort() {
        viewModelScope.launch {
            val list = _listToSort.value!!.toMutableList()
            val n = list.size

            if (gap == 0) {
                for (i in list.indices) {
                    list[i] = list[i].copy(isSorted = true, color = Color.Gray)
                }
                _listToSort.value = list.toList()
                return@launch
            }

            if (currentComparisonIndex + gap < n) {
                list[currentComparisonIndex] = list[currentComparisonIndex].copy(isCurrentlyCompared = true)
                list[currentComparisonIndex + gap] = list[currentComparisonIndex + gap].copy(isCurrentlyCompared = true)
                _listToSort.value = list.toList()

                delay(800)

                if (list[currentComparisonIndex].value > list[currentComparisonIndex + gap].value) {
                    val temp = list[currentComparisonIndex]
                    list[currentComparisonIndex] = list[currentComparisonIndex + gap]
                    list[currentComparisonIndex + gap] = temp
                    _listToSort.value = list.toList()

                    delay(800)
                }

                list[currentComparisonIndex] = list[currentComparisonIndex].copy(isCurrentlyCompared = false)
                list[currentComparisonIndex + gap] = list[currentComparisonIndex + gap].copy(isCurrentlyCompared = false)
                _listToSort.value = list.toList()

                currentComparisonIndex++
            } else {
                gap /= 2
                currentComparisonIndex = 0
            }

        }
    }

    fun restartShellSort() {
        viewModelScope.launch {
            currentSortingJob?.cancel()


            _animationSteps.value = ""

            _listToSort.value = originalList.map { it.copy(isSorted = false, isCurrentlyCompared = false, color = Color.Transparent) }

            startShellSorting()
        }
    }

}