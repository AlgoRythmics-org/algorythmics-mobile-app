package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class BinarySearchViewModel : ViewModel() {

    private val _listToSearch = MutableLiveData<MutableList<ListUiItem>>()
    val listToSearch: LiveData<MutableList<ListUiItem>> get() = _listToSearch

    private val _searchResult = MutableLiveData<Int?>()
    val searchResult: LiveData<Int?> get() = _searchResult

    fun startBinarySearch(searchNumber: Int) {
        viewModelScope.launch {
            _searchResult.value = null
            val list = _listToSearch.value ?: return@launch
            var low = 0
            var high = list.size - 1
            var foundIndex: Int? = null

            val comparisons = mutableListOf<Int>() // Összehasonlítások listája

            while (low <= high) {
                val mid = (low + high) / 2
                val midValue = list[mid].value

                // Összehasonlítás tárolása
                comparisons.add(mid)

                // Színzés előkészítése
                val updatedList = list.mapIndexed { index, it ->
                    if (index == mid) it.copy(isCurrentlyCompared = true)
                    else if (index < low || index > high) it.copy(isCurrentlyCompared = false, isFound = false)
                    else it
                }
                _listToSearch.value = updatedList.toMutableList()

                // Várakozás rövid ideig, hogy látható legyen a színváltozás
                delay(1000)

                // Keresés
                if (midValue == searchNumber) {
                    foundIndex = mid
                    break
                } else if (midValue < searchNumber) {
                    low = mid + 1
                } else {
                    high = mid - 1
                }
            }

            foundIndex?.let { index ->
                // Az összehasonlítások alapján a lista állapotának frissítése
                val updatedList = list.mapIndexed { idx, it ->
                    if (comparisons.contains(idx)) {
                        if (idx == index) it.copy(isFound = true, isCurrentlyCompared = false)
                        else it.copy(isCurrentlyCompared = true, isFound = false)
                    } else {
                        it.copy(isCurrentlyCompared = false, isFound = false)
                    }
                }
                _listToSearch.value = updatedList.toMutableList()
                _searchResult.value = index
            }
        }
    }

    init {
        val list = mutableListOf<ListUiItem>()
        repeat(10) {
            list.add(
                ListUiItem(
                    id = it,
                    isCurrentlyCompared = false,
                    value = Random.nextInt(100)
                )
            )
        }
        // Rendezzük a listát, hogy a bináris keresés működjön
        list.sortBy { it.value }
        _listToSearch.value = list
    }
}