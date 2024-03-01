package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.LinearSearchUseCase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class LinearSearchViewModel (private val linearSearchUseCase: LinearSearchUseCase = LinearSearchUseCase()) : ViewModel() {

    private val _listToSearch = MutableLiveData<MutableList<ListUiItem>>()
    val listToSearch: LiveData<MutableList<ListUiItem>> get() = _listToSearch

    private val _searchResult = MutableLiveData<Int?>()
    val searchResult: LiveData<Int?> get() = _searchResult

    init {
        val list = mutableListOf<ListUiItem>()
        repeat(9) {
            list.add(
                ListUiItem(
                    id = it,
                    isCurrentlyCompared = false,
                    value = Random.nextInt(100)
                )
            )
        }
        _listToSearch.value = list
    }

    fun startLinearSearch(searchNumber: Int) {
        viewModelScope.launch {
            _searchResult.value = null // Alaphelyzetbe állítjuk az eredményt
            val list = _listToSearch.value ?: return@launch
            list.forEachIndexed { index, item ->
                item.isCurrentlyCompared = true // Az aktuálisan vizsgált elem piros
                _listToSearch.value = list.toMutableList() // Frissítjük a listát
                delay(1000) // Várakozás az animáció miatt
                if (item.value == searchNumber) {
                    _searchResult.value = index // Ha megtaláljuk a keresett számot, beállítjuk az eredményt
                    item.isCurrentlyCompared = false // Az aktuálisan vizsgált elem visszakapja eredeti színét
                    _listToSearch.value = list.toMutableList() // Frissítjük a listát
                    return@launch
                }
                item.isCurrentlyCompared = false // Az aktuálisan vizsgált elem visszakapja eredeti színét
                _listToSearch.value = list.toMutableList() // Frissítjük a listát
                delay(1000) // Várakozás az animáció miatt
            }
        }
    }

}