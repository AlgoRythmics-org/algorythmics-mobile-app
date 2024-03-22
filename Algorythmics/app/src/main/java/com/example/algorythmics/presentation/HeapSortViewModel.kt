package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.HeapSortUseCase
import kotlinx.coroutines.launch

class HeapSortViewModel(private val heapSortUseCase: HeapSortUseCase = HeapSortUseCase()) : ViewModel() {

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    init {
        initializeList()
    }

    private fun initializeList() {
        val list = mutableListOf<ListUiItem>()
        for (i in 0 until 9) {
            list.add(
                ListUiItem(
                    id = i,
                    isCurrentlyCompared = false,
                    value = (1..150).random() // Random szám generálás 1 és 150 között
                )
            )
        }
        _listToSort.value = list
    }

    fun startHeapSorting() {
        viewModelScope.launch {
            val originalList = _listToSort.value ?: return@launch
            val sortedList = heapSortUseCase.heapSort(originalList.map { it.value }.toMutableList())
            sortedList.collect { sortInfoList ->
                val newList = originalList.mapIndexed { index, item ->
                    ListUiItem(
                        id = item.id,
                        isCurrentlyCompared = false,
                        value = sortInfoList[index].currentItem,
                        isFound = sortInfoList[index].shouldSwap,
                        isInitialColorNeeded = !sortInfoList[index].hadNoEffect
                    )
                }
                _listToSort.value = newList
            }
        }
    }
}