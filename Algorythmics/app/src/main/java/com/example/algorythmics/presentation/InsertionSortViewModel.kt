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

    fun startInsertionSorting() {
        viewModelScope.launch {
            _listToSort.value?.let { list ->
                var lastSortedIndex = 0
                insertionSortUseCase(list.map { it.value }.toMutableList()).collect { swapInfo ->
                    val currentItemIndex = swapInfo.currentItem
                    val newList = _listToSort.value!!.toMutableList()

                    // Az aktuálisan összehasonlított elemeket jelöljük pirosnak
                    for (i in lastSortedIndex until currentItemIndex + 2) {
                        newList[i] = newList[i].copy(isCurrentlyCompared = true)
                    }

                    if (swapInfo.shouldSwap) {
                        val firstItem = newList.removeAt(currentItemIndex)
                        newList.add(currentItemIndex + 1, firstItem)
                    }

                    if (swapInfo.hadNoEffect) {
                        // Visszaállítjuk a szürke színt azokra az elemekre, amelyek nem kerültek cserére
                        for (i in lastSortedIndex until currentItemIndex + 2) {
                            newList[i] = newList[i].copy(isCurrentlyCompared = false)
                        }
                    }

                    // A rendezett elemek visszaállítása szürkére, kivéve az aktuális elemeket
                    for (i in lastSortedIndex until currentItemIndex) {
                        newList[i] = newList[i].copy(isSorted = true)
                    }

                    // Az új elem rendezettségének beállítása
                    if (swapInfo.hadNoEffect) {
                        newList[currentItemIndex] = newList[currentItemIndex].copy(isSorted = true)
                    }

                    _listToSort.value = newList
                    lastSortedIndex = currentItemIndex + 1
                }
            }
        }
    }



}