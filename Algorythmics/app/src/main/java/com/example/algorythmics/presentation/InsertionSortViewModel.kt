package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.InsertionSortUseCase
import kotlinx.coroutines.launch
import kotlin.random.Random

class InsertionSortViewModel(private val insertionSortUseCase: InsertionSortUseCase = InsertionSortUseCase()) : ViewModel() {

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

    fun startInsertionSorting() {
        viewModelScope.launch {
            _listToSort.value?.let { list ->
                insertionSortUseCase(list.map { it.value }.toMutableList()).collect { swapInfo ->
                    val currentItemIndex = swapInfo.currentItem
                    val newList = _listToSort.value!!.toMutableList() // Frissítettük az értéket

                    if (currentItemIndex >= newList.size || currentItemIndex + 1 >= newList.size) return@collect // Index ellenőrzés

                    newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = true)
                    newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = true)

                    if (swapInfo.shouldSwap) {
                        val firstItem = newList.removeAt(currentItemIndex) // Elem eltávolítása
                        newList.add(currentItemIndex + 1, firstItem) // Elem beszúrása a megfelelő helyre
                    }

                    if (swapInfo.hadNoEffect) {
                        newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                        newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                    }
                    _listToSort.value = newList
                }
            }
        }
    }
}