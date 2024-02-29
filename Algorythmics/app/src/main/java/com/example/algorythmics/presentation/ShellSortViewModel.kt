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

                    // Az összehasonlítás alatt álló elemek pirossá tétele
                    val itemToCompareIndex = swapInfo.itemToSwap
                    if (itemToCompareIndex != -1) {
                        // Elemek cseréje
                        val temp = newList[currentItemIndex]
                        newList[currentItemIndex] = newList[itemToCompareIndex]
                        newList[itemToCompareIndex] = temp

                        // Ha nincs több cserélnivaló, akkor állítjuk pirosra az összes elemet
                        if (itemToCompareIndex == -1) {
                            newList.forEachIndexed { index, listUiItem ->
                                newList[index] = listUiItem.copy(isCurrentlyCompared = true)
                            }
                        } else {
                            // Ha még vannak cserék hátra, csak az éppen cserélt elemeket állítjuk pirosra
                            newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = true)
                            newList[itemToCompareIndex] = newList[itemToCompareIndex].copy(isCurrentlyCompared = true)
                        }
                    } else {
                        // Az összehasonlítás befejeződött, minden elemet visszaállítjuk
                        newList.forEachIndexed { index, listUiItem ->
                            newList[index] = listUiItem.copy(isCurrentlyCompared = false)
                        }
                    }

                    _listToSort.value = newList
                }
            }
        }
    }


}
