package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.SelectionSortUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class SelectionSortViewModel
    (private val selectionSortUseCase: SelectionSortUseCase = SelectionSortUseCase()) : ViewModel() {
    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

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
    }

    fun startSelectionSorting() {
        viewModelScope.launch {
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

                val temp = list[i]
                list[i] = list[minIndex]
                list[minIndex] = temp

                list[i] = list[i].copy(isCurrentlyCompared = false, isSorted = true)
                _listToSort.value = list.toList()
                delay(500)
            }

            list[n - 1] = list[n - 1].copy(isCurrentlyCompared = false, isSorted = true)
            _listToSort.value = list.toList()
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



}
