package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.HeapSortUseCase
import com.example.algorythmics.use_case.swap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class HeapSortViewModel(private val heapSortUseCase: HeapSortUseCase = HeapSortUseCase()) : ViewModel() {

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

    fun startHeapSorting() {
        viewModelScope.launch {
            val list = _listToSort.value.orEmpty().toMutableList()

            heapSortUseCase(list.map { it.value }.toMutableList()).collect { sortInfo ->
                val newList = _listToSort.value!!.toMutableList()

                val currentItemIndex = sortInfo.currentItem
                val shouldSwap = sortInfo.shouldSwap
                val hadNoEffect = sortInfo.hadNoEffect

                if (currentItemIndex >= newList.size || currentItemIndex + 1 >= newList.size) return@collect

                // Aktuális elemek pirossá tételére
                newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = true)
                newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = true)

                // Csere esetén
                if (shouldSwap) {
                    val firstItem = newList.removeAt(currentItemIndex)
                    newList.add(currentItemIndex + 1, firstItem)
                }

                // Ha nem volt hatása a cserének
                if (hadNoEffect) {
                    newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                }

                // Lista frissítése
                _listToSort.value = newList
            }
        }
    }
}