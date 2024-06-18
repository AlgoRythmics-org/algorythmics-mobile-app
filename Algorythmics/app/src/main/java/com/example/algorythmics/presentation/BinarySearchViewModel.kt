package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
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

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> get() = _comparisonMessage

    private var isSearching = false

    private var low = 0
    private var high = 0
    private var mid = 0
    private var searchNumber = 0
    private var foundIndex: Int? = null

    init {
        val list = mutableListOf<ListUiItem>()
        repeat(9) {
            list.add(
                ListUiItem(
                    id = it,
                    isCurrentlyCompared = false,
                    value = Random.nextInt(100),
                )
            )
        }
        // Sort the list for binary search
        list.sortBy { it.value }
        _listToSearch.value = list
    }

    fun startBinarySearch(searchNumber: Int) {
        viewModelScope.launch {
            _searchResult.value = null
            val list = _listToSearch.value ?: return@launch
            var low = 0
            var high = list.size - 1
            var foundIndex: Int? = null

            while (low <= high) {
                val mid = (low + high) / 2
                val midValue = list[mid].value

                // Update the UI to highlight the current middle element and dim others
                val updatedList = list.mapIndexed { index, it ->
                    when {
                        index == mid -> it.copy(isCurrentlyCompared = true, color = Color.Red)
                        index < low || index > high -> it.copy(isSorted = true)
                        else -> it.copy(isCurrentlyCompared = false)
                    }
                }.toMutableList()

                updateList(updatedList) // Update the LiveData with updated list

                val searchMessage = "Középső elem  ${midValue} összehasonlítva a keresett elemmel $searchNumber"
                _comparisonMessage.postValue(searchMessage)

                // Wait for visual effect
                delay(1500)

                // Check if the middle element is the target
                if (midValue == searchNumber) {
                    foundIndex = mid
                    val foundMessage = "Keresett elem megtalálva: ${midValue} a pozíció: $mid"
                    _comparisonMessage.postValue(foundMessage)
                    break
                }

                // Update search bounds
                if (midValue < searchNumber) {
                    low = mid + 1
                } else {
                    high = mid - 1
                }
            }

            // Highlight the found element or reset colors if not found
            val finalUpdatedList = list.mapIndexed { index, it ->
                when {
                    foundIndex != null && index == foundIndex -> it.copy(isFound = true, isCurrentlyCompared = false)
                    else -> it.copy(isCurrentlyCompared = false)
                }
            }.toMutableList()

            updateList(finalUpdatedList) // Update the LiveData with final updated list
            _searchResult.value = foundIndex

            if (foundIndex == null) {
                val notFoundMessage = "Keresett elem ($searchNumber) nem található a listában."
                _comparisonMessage.postValue(notFoundMessage)
            }
        }
    }

    private var currentStepIndex = -1


    fun stepBinarySearch(searchNumber: Int) {
        if (!isSearching) {
            isSearching = true
            low = 0
            high = _listToSearch.value?.size?.minus(1) ?: 0
            currentStepIndex = 0
            foundIndex = null
            this.searchNumber = searchNumber
            _searchResult.value = null
        }

        viewModelScope.launch {
            val list = _listToSearch.value ?: return@launch

            if (low > high) {
                // Search ended, number not found
                _searchResult.value = null
                isSearching = false
                return@launch
            }

            when (currentStepIndex) {
                0 -> {
                    mid = (low + high) / 2
                    // Highlight the current middle element and dim others
                    val updatedList = list.mapIndexed { index, it ->
                        when {
                            index == mid -> it.copy(isCurrentlyCompared = true, color = Color.Red)
                            index < low || index > high -> it.copy(isSorted = true, color = Color.Gray)
                            else -> it.copy(isCurrentlyCompared = false)
                        }
                    }.toMutableList()
                    updateList(updatedList)
                    currentStepIndex = 1
                }
                1 -> {
                    val midValue = list[mid].value
                    if (midValue == searchNumber) {
                        // Number found
                        foundIndex = mid
                        isSearching = false
                        val updatedList = list.mapIndexed { index, it ->
                            if (index == mid) it.copy(isCurrentlyCompared = false, color = Color.Green)
                            else it.copy(isCurrentlyCompared = false)
                        }.toMutableList()
                        updateList(updatedList)
                        _searchResult.value = mid
                    } else {
                        if (midValue < searchNumber) {
                            low = mid + 1
                        } else {
                            high = mid - 1
                        }
                        // Reset the middle element's color and update the dimmed elements
                        val updatedList = list.mapIndexed { index, it ->
                            when {
                                index == mid -> it.copy(isCurrentlyCompared = false, color = Color.Black)
                                index < low || index > high -> it.copy(isSorted = true, color = Color.Gray)
                                else -> it.copy(isCurrentlyCompared = false)
                            }
                        }.toMutableList()
                        updateList(updatedList)
                        currentStepIndex = 0
                    }
                }
            }
        }
    }
    private fun updateList(updatedList: MutableList<ListUiItem>) {
        _listToSearch.value = updatedList
    }
}