package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.fragments.course.SelectionSortListAdapter
import com.example.algorythmics.retrofit.models.SelectionSortInfo
import com.example.algorythmics.use_case.SelectionSortUseCase
import kotlinx.coroutines.launch
import kotlin.random.Random

class SelectionSortViewModel
    (private val selectionSortUseCase: SelectionSortUseCase = SelectionSortUseCase()) : ViewModel() {
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

    fun startSelectionSorting() {
        viewModelScope.launch {
            val initialList = _listToSort.value!!.map { it.copy(isCurrentlyCompared = false) }
            _listToSort.value = initialList

            selectionSortUseCase(_listToSort.value!!.map { it.value }
                .toMutableList()).collect { swapInfo ->
                val currentItemIndex = swapInfo.currentItem

                if (currentItemIndex >= 0 && currentItemIndex < _listToSort.value!!.size - 1) {
                    val newList = _listToSort.value!!.toMutableList()

                    if (swapInfo.shouldSwap) {
                        val firstItem = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                        newList[currentItemIndex] =
                            newList[swapInfo.itemToSwap].copy(isCurrentlyCompared = false)
                        newList[swapInfo.itemToSwap] = firstItem
                    }
                    // Elemek cseréje
                    val temp = newList[currentItemIndex]
                    newList[currentItemIndex] = newList[swapInfo.itemToSwap]
                    newList[swapInfo.itemToSwap] = temp

                    // Csak az éppen cserélt elemet állítjuk pirosra
                    newList[currentItemIndex] =
                        newList[currentItemIndex].copy(isCurrentlyCompared = true)

                    newList[currentItemIndex] =
                        newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[swapInfo.itemToSwap] =
                        newList[swapInfo.itemToSwap].copy(isCurrentlyCompared = false)
                    _listToSort.value = newList
                    // Lista frissítése
                    _listToSort.value = newList
                }
            }

            // Sortálás végén az összes elem pirosra állítása
            val finalList = _listToSort.value!!.mapIndexed { index, item ->
                if (index < _listToSort.value!!.size - 1) {
                    item.copy(isCurrentlyCompared = true)
                } else {
                    item.copy(isCurrentlyCompared = false) // Utolsó elem szürke lesz
                }
            }
            _listToSort.value = finalList
        }
    }

}
