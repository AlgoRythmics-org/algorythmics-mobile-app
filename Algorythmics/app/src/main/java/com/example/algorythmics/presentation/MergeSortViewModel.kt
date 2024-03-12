package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.MergeSortUseCase
import kotlinx.coroutines.launch




class MergeSortViewModel(private val mergeSortUseCase: MergeSortUseCase = MergeSortUseCase()) : ViewModel() {

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
                    value = (1..150).random(),
                    needsColorUpdate = false // Alapértelmezetten nincs színezési igény
                )
            )
        }
        _listToSort.value = list
    }

    fun startMergeSorting() {
        viewModelScope.launch {
            val originalList = _listToSort.value ?: return@launch
            val sortedList = mergeSortUseCase.mergeSort(originalList.map { it.value }.toMutableList(), 0, originalList.size - 1)
            sortedList.collect { sortedValues ->
                val newList = sortedValues.mapIndexed { index, value ->
                    val oldItem = _listToSort.value?.getOrNull(index)
                    ListUiItem(
                        id = index,
                        isCurrentlyCompared = oldItem?.isCurrentlyCompared ?: false,
                        value = value,
                        needsColorUpdate = oldItem?.isCurrentlyCompared == true // A színezési igényt a hasonlítás alapján állítjuk be
                    )
                }
                _listToSort.value = newList
            }
        }
    }
}