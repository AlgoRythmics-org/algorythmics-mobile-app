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

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    init {
        val list = mutableListOf<ListUiItem>()
        for ((i, item) in insertionSortUseCase.items.withIndex()) {
            list.add(
                ListUiItem(
                    id = i,
                    value = item,
                    isSorted = (i == 0)
                )
            )
        }
        _listToSort.value = list
    }

    fun startInsertionSorting() {
        viewModelScope.launch {
            _listToSort.value?.let { initialList ->
                val newList = initialList.toMutableList()
                for (i in 1 until newList.size) {
                    var j = i
                    _listToSort.value = newList.toList()
                    delay(1500)

                    newList[j] = newList[j].copy(isCurrentlyCompared = true)
                    _listToSort.value = newList.toList()
                    delay(800)

                    while (j > 0 && newList[j].value < newList[j - 1].value) {

                        val temp = newList[j]
                        newList[j] = newList[j - 1]
                        newList[j - 1] = temp

                        _listToSort.value = newList.toList()
                        delay(800)

                        j--
                    }
                    newList[j] = newList[j].copy(isCurrentlyCompared = false, isSorted = true)
                    _listToSort.value = newList.toList()
                    delay(800)
                }

                _listToSort.value = newList.map { it.copy(isSorted = true) }
            }
        }
    }

    fun shuffleList() {
        val list = _listToSort.value ?: return
        val shuffledList = list.shuffled()
        _listToSort.value = shuffledList.toMutableList()
    }
}