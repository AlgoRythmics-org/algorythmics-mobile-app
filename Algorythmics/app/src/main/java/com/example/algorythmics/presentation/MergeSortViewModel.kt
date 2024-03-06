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

    fun startMergeSorting() {
        viewModelScope.launch {
            _listToSort.value?.let { list ->
                mergeSortUseCase(list.map { it.value }.toMutableList()).collect { swapInfo ->
                    val currentItemIndex = swapInfo.currentItem
                    val newList = _listToSort.value!!.toMutableList()

                    if (currentItemIndex >= newList.size || currentItemIndex + 1 >= newList.size) return@collect

                    if (swapInfo.shouldSwap) {
                        val temp = newList[currentItemIndex]
                        newList[currentItemIndex] = newList[currentItemIndex + 1]
                        newList[currentItemIndex + 1] = temp
                    }

                    _listToSort.value = newList
                }
            }
        }
    }

}