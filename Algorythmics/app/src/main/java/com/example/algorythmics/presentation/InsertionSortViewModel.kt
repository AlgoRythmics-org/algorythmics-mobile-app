package com.example.algorythmics.presentation

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

    init {
        val list = mutableListOf<ListUiItem>()
        for ((i, item) in insertionSortUseCase.items.withIndex()) {
            list.add(
                ListUiItem(
                    id = i,
                    value = item,
                   // isSorted = (i == 0)
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
                newList[0] = newList[0].copy(isSorted = true) // Set the first item as sorted (gray)
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

                        val temp = newList[j]
                        newList[j] = newList[j - 1]
                        newList[j - 1] = temp

                        _sortedList.value = newList.toList()
                        delay(800)

                        j--
                    }
                    newList[j] = newList[j].copy(isCurrentlyCompared = false, isSorted = true)
                    _sortedList.value = newList.toList()
                    delay(800)
                }

                _sortedList.value = newList.map { it.copy(isSorted = true) }
            }
        }
    }

    fun shuffleList() {
        val list = _originalList.value ?: return
        val shuffledList = list.shuffled()
        _sortedList.value = shuffledList
    }
}