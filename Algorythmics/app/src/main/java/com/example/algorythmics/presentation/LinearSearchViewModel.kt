package com.example.algorythmics.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.LinearSearchUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class LinearSearchViewModel  : ViewModel() {

    private val _listToSearch = MutableLiveData<MutableList<ListUiItem>>()
    val listToSearch: LiveData<MutableList<ListUiItem>> get() = _listToSearch

    private val _searchResult = MutableLiveData<Int?>()
    val searchResult: LiveData<Int?> get() = _searchResult

    fun startLinearSearch(searchNumber: Int) {
        viewModelScope.launch {
            _searchResult.value = null
            val list = _listToSearch.value ?: return@launch
            var foundIndex: Int? = null

            for ((index, item) in list.withIndex()) {
                // Színzés előkészítése
                val updatedList = list.mapIndexed { idx, it ->
                    if (idx == index) it.copy(isCurrentlyCompared = true)
                    else if (idx < index) it.copy(isCurrentlyCompared = false, isFound = false)
                    else it
                }
                _listToSearch.value = updatedList.toMutableList()

                // Várakozás rövid ideig, hogy látható legyen a színváltozás
                delay(1000)

                // Keresés
                if (item.value == searchNumber) {
                    foundIndex = index
                    break
                }
            }

            foundIndex?.let { index ->
                // Az utolsó találat kiszínezése
                val updatedList = list.mapIndexed { idx, it ->
                    if (idx == index) it.copy(isFound = true)
                    else it.copy(isCurrentlyCompared = false)
                }
                _listToSearch.value = updatedList.toMutableList()
                _searchResult.value = index
            }
        }
    }


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
}