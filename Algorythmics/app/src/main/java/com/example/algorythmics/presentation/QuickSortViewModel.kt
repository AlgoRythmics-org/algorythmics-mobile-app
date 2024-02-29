package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.QuickSortUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class QuickSortViewModel (private val quickSortUseCase: QuickSortUseCase = QuickSortUseCase()) : ViewModel() {

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    init {
        val list = mutableListOf<ListUiItem>()
        for (i in 0 until 9) {
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

    fun startQuickSorting() {
        viewModelScope.launch {
            val initialList = _listToSort.value!!.map { it.copy(isCurrentlyCompared = false) }
            _listToSort.value = initialList

            val processList = mutableListOf<ListUiItem>()

            quickSortUseCase(_listToSort.value!!.map { it.value }.toMutableList(), 0, _listToSort.value!!.size - 1)
                .collect { quickSortInfoList ->
                    quickSortInfoList.forEachIndexed { index, quickSortInfo ->
                        val partitionIndex = quickSortInfo.partitionIndex
                        val newList = quickSortInfo.list.mapIndexed { listIndex, value ->
                            ListUiItem(
                                id = listIndex,
                                isCurrentlyCompared = listIndex == partitionIndex,
                                value = value
                            )
                        }
                        processList.clear()
                        processList.addAll(newList)

                        _listToSort.value = processList

                        delay(3000)

                        if (index == quickSortInfoList.size - 1) {
                            // Az utolsó iterációban állítjuk pirosra az összes elemet
                            val finalList = processList.map { it.copy(isCurrentlyCompared = true) }
                            _listToSort.value = finalList
                        }
                    }
                }
        }
    }



}