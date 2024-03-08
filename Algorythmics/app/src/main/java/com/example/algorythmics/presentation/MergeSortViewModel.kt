package com.example.algorythmics.presentation


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.example.algorythmics.use_case.MergeSortUseCase

import kotlinx.coroutines.launch
import kotlin.random.Random


class MergeSortViewModel(private val mergeSortUseCase: MergeSortUseCase = MergeSortUseCase()) : ViewModel() {

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _sortedList = MutableLiveData<List<ListUiItem>>()
    val sortedList: LiveData<List<ListUiItem>> get() = _sortedList

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
        _listToSort.value = list.sortedBy { it.value }
    }

    fun startMergeSorting() {
        viewModelScope.launch {
            _listToSort.value?.map { it.value }?.toMutableList()?.let { list ->
                mergeSortUseCase(list).collect { sortedList ->
                    _sortedList.value = sortedList.mapIndexed { index, value ->
                        _listToSort.value!![index].copy(value = value)
                    }
                }
            }
        }
    }
}
