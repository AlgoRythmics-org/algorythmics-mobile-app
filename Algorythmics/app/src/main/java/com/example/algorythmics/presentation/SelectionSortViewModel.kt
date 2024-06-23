package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.SelectionSortUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class SelectionSortViewModel
    (private val selectionSortUseCase: SelectionSortUseCase = SelectionSortUseCase()) : ViewModel() {

    private var originalList = mutableListOf<ListUiItem>()
    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> get() = _comparisonMessage

    private var currentSortingJob: Job? = null

    private var minIndex = 0
    private var i = 0
    private var step = 0

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


    fun startSelectionSorting() {
        currentSortingJob?.cancel()

        currentSortingJob = viewModelScope.launch {
            val initialList = _listToSort.value!!.map { it.copy(isCurrentlyCompared = false, isSorted = false) }
            _listToSort.value = initialList

            val list = _listToSort.value!!.toMutableList()
            val n = list.size

            for (i in 0 until n - 1) {
                var minIndex = i

                for (j in i until n) {
                    list[j] = list[j].copy(isCurrentlyCompared = true)
                    _listToSort.value = list.toList()
                    delay(500)

                    if (list[j].value < list[minIndex].value) {
                        minIndex = j
                    }

                    list[j] = list[j].copy(isCurrentlyCompared = false)
                    _listToSort.value = list.toList()
                }

                list[minIndex] = list[minIndex].copy(isCurrentlyCompared = true)
                _listToSort.value = list.toList()
                delay(800)

                val selectionMessage = "Legkisebb elem kiválasztva: ${list[minIndex].value}"
                _comparisonMessage.postValue(selectionMessage)

                val temp = list[i]
                list[i] = list[minIndex]
                list[minIndex] = temp

                list[i] = list[i].copy(isCurrentlyCompared = false, isSorted = true)
                _listToSort.value = list.toList()
                delay(500)

                val sortedMessage = "Rendezett elem: ${list[i].value} a pozíció: $i"
                _comparisonMessage.postValue(sortedMessage)

                val step = "Külső ciklus index: $i, Kiválasztott elem: ${list[minIndex].value}, Rendezett elem: ${list[i].value}"
                _animationSteps.postValue(step)
            }

            list[n - 1] = list[n - 1].copy(isCurrentlyCompared = false, isSorted = true)
            _listToSort.value = list.toList()

            val finalSortedMessage = "Rendezett elem: ${list[n - 1].value} a pozíció: ${n - 1}"
            _comparisonMessage.postValue(finalSortedMessage)
        }
    }


    fun stepSelectionSorting() {
        viewModelScope.launch {
            val list = _listToSort.value!!.toMutableList()
            val n = list.size

            if (i >= n) {
                list[n - 1] = list[n - 1].copy(isCurrentlyCompared = false, isSorted = true)
                _listToSort.value = list.toList()
                return@launch
            }

            when (step) {
                0 -> {
                    minIndex = i

                    for (j in i until n) {
                        list[j] = list[j].copy(isCurrentlyCompared = true)
                        _listToSort.value = list.toList()
                        delay(500)

                        if (list[j].value < list[minIndex].value) {
                            minIndex = j
                        }

                        list[j] = list[j].copy(isCurrentlyCompared = false)
                        _listToSort.value = list.toList()
                    }

                    list[minIndex] = list[minIndex].copy(isCurrentlyCompared = true)
                    _listToSort.value = list.toList()
                    step = 1
                }
                1 -> {
                    val temp = list[i]
                    list[i] = list[minIndex]
                    list[minIndex] = temp

                    list[i] = list[i].copy(isCurrentlyCompared = false, isSorted = true)
                    _listToSort.value = list.toList()
                    delay(500)

                    i++
                    step = 0
                }
            }
        }
    }

    fun shuffleList() {
        val list = _listToSort.value ?: return
        val shuffledList = list.shuffled()
        _listToSort.value = shuffledList.toMutableList()
    }


    fun restartSelectionSort() {
        viewModelScope.launch {
            currentSortingJob?.cancel()

            minIndex = 0
            i = 0
            step = 0

            _animationSteps.value = ""

            _listToSort.value = originalList.map { it.copy(isSorted = false, isCurrentlyCompared = false, color = Color.Transparent) }

            startSelectionSorting()
        }
    }

}
