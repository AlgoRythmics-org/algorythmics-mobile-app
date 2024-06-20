package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

class LinearSearchViewModel  : ViewModel() {

    private var originalList = mutableListOf<ListUiItem>()
    private val _listToSearch = MutableLiveData<MutableList<ListUiItem>>()
    val listToSearch: LiveData<MutableList<ListUiItem>> get() = _listToSearch

    private val _searchResult = MutableLiveData<Int?>()
    val searchResult: LiveData<Int?> get() = _searchResult

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> get() = _comparisonMessage

    private var totalSteps = 0
    private var currentStep = 0
    private var isSearching: Boolean = false

    private val _isStepButtonEnabled = MutableLiveData<Boolean>()
    val isStepButtonEnabled: LiveData<Boolean> get() = _isStepButtonEnabled

    private var currentSortingJob: Job? = null

    // searchPosition a megtalált elem pozícióját tárolja
    private var searchPosition: Int? = null

    fun startLinearSearch(searchNumber: Int) {
        currentSortingJob?.cancel()

        currentSortingJob = viewModelScope.launch {
            _searchResult.value = null
            val list = _listToSearch.value ?: return@launch
            var foundIndex: Int? = null

            for ((index, item) in list.withIndex()) {

                val updatedList = list.mapIndexed { idx, it ->
                    if (idx == index) it.copy(isCurrentlyCompared = true)
                    else if (idx < index) it.copy(isCurrentlyCompared = false, isFound = false)
                    else it
                }
                _listToSearch.value = updatedList.toMutableList()

                val searchMessage = "Elem ${item.value} összehasonlítva a keresett számmal: $searchNumber"
                _comparisonMessage.postValue(searchMessage)

                delay(1000)

                // Search
                if (item.value == searchNumber) {
                    foundIndex = index
                    val foundMessage = "Keresett elem megtalálva: ${item.value} a pozíció: $index"
                    _comparisonMessage.postValue(foundMessage)
                    break
                }
            }

            foundIndex?.let { index ->
                val updatedList = list.mapIndexed { idx, it ->
                    if (idx == index) it.copy(isFound = true)
                    else it.copy(isCurrentlyCompared = false)
                }
                _listToSearch.value = updatedList.toMutableList()
                _searchResult.value = index
            } ?: run {
                val notFoundMessage = "Keresett elem ($searchNumber) nem található a listában."
                _comparisonMessage.postValue(notFoundMessage)
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
        _listToSearch.value = list
        originalList = list
    }

    fun shuffleList() {
        val list = _listToSearch.value ?: return
        val shuffledList = list.shuffled()
        _listToSearch.value = shuffledList.toMutableList()
    }

    fun stepLinearSearch(searchNumber: Int): Boolean {
        val list = _listToSearch.value ?: return false

        if (!isSearching) {
            _searchResult.value = null
            totalSteps = list.size
            currentStep = 0
            isSearching = true
            _isStepButtonEnabled.value = true
        }

        if (!isSearching) {
            return false
        }

        if (currentStep < totalSteps) {
            val item = list[currentStep]
            val updatedList = list.mapIndexed { index, it ->
                if (index == currentStep) {
                    it.copy(isCurrentlyCompared = true)
                } else if (index < currentStep) {
                    it.copy(isCurrentlyCompared = false, isFound = false)
                } else {
                    it
                }
            }
            _listToSearch.value = updatedList.toMutableList()

            if (item.value == searchNumber) {
                val finalUpdatedList = list.mapIndexed { index, it ->
                    if (index == currentStep) {
                        it.copy(isFound = true, isCurrentlyCompared = false)
                    } else {
                        it.copy(isCurrentlyCompared = false)
                    }
                }
                _listToSearch.value = finalUpdatedList.toMutableList()
                _searchResult.value = currentStep
                isSearching = false
                _isStepButtonEnabled.value = false
                return true
            }

            currentStep++
        } else {
            isSearching = false
            _isStepButtonEnabled.value = false
        }

        return false
    }


        fun resetStepButton() {
        _isStepButtonEnabled.value = true
    }

    fun restartLinearSearch(searchNumber: Int) {
        viewModelScope.launch {
            currentSortingJob?.cancel()

            totalSteps = 0
            currentStep = 0
            isSearching = false
            searchPosition = null

            _searchResult.value = null
            _comparisonMessage.value = ""

            _listToSearch.value = originalList.map { it.copy(isCurrentlyCompared = false, isFound = false) }.toMutableList()

            startLinearSearch(searchNumber)
        }
    }
}