package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.BinarySearchUseCase
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.launch
import kotlin.random.Random

class BinarySearchViewModel : ViewModel() {

    private val _sortedListToSearch = MutableLiveData<MutableList<ListUiItem>>()
    val sortedListToSearch: LiveData<MutableList<ListUiItem>> get() = _sortedListToSearch

    private val _searchResult = MutableLiveData<Int?>()
    val searchResult: LiveData<Int?> get() = _searchResult

    init {
        val random = Random(System.currentTimeMillis()) // Inicializáljuk a random generátort az aktuális idő alapján
        val randomList = MutableList(10) { ListUiItem(id = it, isCurrentlyCompared = false, value = random.nextInt(100)) }
        val sortedList = randomList.sortedBy { it.value }.toMutableList() // Rendezzük a listát növekvő sorrendben
        _sortedListToSearch.value = sortedList
    }

    fun startBinarySearch(searchNumber: Int) {
        viewModelScope.launch {
            _searchResult.value = null // Alapértelmezett eredmény beállítása
            val list = _sortedListToSearch.value ?: return@launch
            var left = 0
            var right = list.size - 1

            while (left <= right) {
                val mid = left + (right - left) / 2
                val midValue = list[mid].value
                if (midValue == searchNumber) {
                    _searchResult.value = mid // Az elem megtalálva
                    return@launch
                }
                if (midValue < searchNumber) {
                    left = mid + 1 // Ha a középső elem kisebb, mint a keresett szám, a jobb oldalra kell mennünk
                } else {
                    right = mid - 1 // Különben a bal oldalra kell mennünk
                }
            }
        }
    }

}