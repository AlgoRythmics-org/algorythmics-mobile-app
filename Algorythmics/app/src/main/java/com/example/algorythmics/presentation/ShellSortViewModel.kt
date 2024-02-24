package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.ShellSortUseCase
import kotlinx.coroutines.launch
import kotlin.random.Random

class ShellSortViewModel (private val shellSortUseCase: ShellSortUseCase = ShellSortUseCase()) : ViewModel() {

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

    fun startShellSorting() {
        viewModelScope.launch {
            shellSortUseCase(_listToSort.value!!.map { it.value }.toMutableList()).collect { swapInfo ->
                val currentItemIndex = swapInfo.currentItem

                if (currentItemIndex >= 0 && currentItemIndex < _listToSort.value!!.size) {
                    val newList = _listToSort.value!!.toMutableList()

                    if (swapInfo.itemToSwap != -1) {
                        val firstItem = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                        newList[currentItemIndex] = newList[swapInfo.itemToSwap].copy(isCurrentlyCompared = false)
                        newList[swapInfo.itemToSwap] = firstItem
                    }

                    _listToSort.value = newList
                }
            }
        }
    }
}