package com.example.algorythmics.presentation

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class LinearSearchViewModel  : ViewModel() {


    private val _listToSearch = MutableLiveData<MutableList<ListUiItem>>()
    val listToSearch: LiveData<MutableList<ListUiItem>> get() = _listToSearch

    private val _searchResult = MutableLiveData<Int?>()
    val searchResult: LiveData<Int?> get() = _searchResult

    private var totalSteps = 0
    private var currentStep = 0
    private var isSearching: Boolean = false


    private val _isStepButtonEnabled = MutableLiveData<Boolean>()
    val isStepButtonEnabled: LiveData<Boolean> get() = _isStepButtonEnabled


    fun startLinearSearch( searchNumber: Int) {
        viewModelScope.launch {
            _searchResult.value = null
            val list = _listToSearch.value ?: return@launch
            var foundIndex: Int? = null

            for ((index, item) in list.withIndex()) {
                // Prepare for comparison coloring
                val updatedList = list.mapIndexed { idx, it ->
                    if (idx == index) it.copy(isCurrentlyCompared = true)
                    else if (idx < index) it.copy(isCurrentlyCompared = false, isFound = false)
                    else it
                }
                _listToSearch.value = updatedList.toMutableList()


                delay(1000)

                // Search
                if (item.value == searchNumber) {
                    foundIndex = index
                    break
                }
            }

            foundIndex?.let { index ->

                val updatedList = list.mapIndexed { idx, it ->
                    if (idx == index) it.copy(isFound = true, color = Color.Green)
                    else it.copy(isCurrentlyCompared = false)
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
        _listToSearch.value = list
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
                it.copy(isCurrentlyCompared = index == currentStep)
            }
            _listToSearch.value = updatedList.toMutableList()

            if (item.value == searchNumber) {
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
}