package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.MergeSortUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class MergeSortViewModel(private val mergeSortUseCase: MergeSortUseCase = MergeSortUseCase()) : ViewModel() {
    private var originalList = mutableListOf<ListUiItem>()

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _mergeSortSteps = MutableLiveData<String>()
    val mergeSortSteps: LiveData<String> get() = _mergeSortSteps

    private var currentStep = 0
    private var mergeSortInProgress = false

    private val _currentStepLiveData = MutableLiveData<Int>()
    val currentStepLiveData: LiveData<Int> get() = _currentStepLiveData


    private var currentSortingJob: Job? = null

    init {
        initializeList()
    }

    private fun initializeList() {
        val list = mutableListOf<ListUiItem>()
        for (i in 0 until 10) {
            list.add(
                ListUiItem(
                    id = i,
                    isCurrentlyCompared = false,
                    value = (1..150).random(),
                    needsColorUpdate = false
                )
            )
        }
        _listToSort.value = list
        originalList = list
    }

    fun startMergeSorting() {
        currentSortingJob?.cancel()
        currentSortingJob =viewModelScope.launch {
            val originalList = _listToSort.value ?: return@launch
            mergeSort(originalList.toMutableList(), 0, originalList.size - 1)
        }
    }

    private suspend fun mergeSort(arr: MutableList<ListUiItem>, l: Int, r: Int) {
        if (l < r) {
            val m = l + (r - l) / 2
            mergeSort(arr, l, m)
            mergeSort(arr, m + 1, r)
            merge(arr, l, m, r)
        }
    }

    private suspend fun merge(arr: MutableList<ListUiItem>, l: Int, m: Int, r: Int) {
        val leftArray = arr.subList(l, m + 1).toMutableList()
        val rightArray = arr.subList(m + 1, r + 1).toMutableList()

        var i = 0
        var j = 0
        var k = l

        while (i < leftArray.size && j < rightArray.size) {
            arr[k].isCurrentlyCompared = true
            arr[k + 1].isCurrentlyCompared = true
            delay(1000)

            if (leftArray[i].value <= rightArray[j].value) {
                arr[k] = leftArray[i].copy()
                i++
            } else {
                arr[k] = rightArray[j].copy()
                j++
            }

            arr[k].isCurrentlyCompared = false
            arr[k].isSorted = true

            val stepMessage = "Összefésülés: ${arr[k].value}"
            _mergeSortSteps.postValue(stepMessage)

            k++
        }

        while (i < leftArray.size) {
            arr[k] = leftArray[i].copy()
            arr[k].isCurrentlyCompared = false
            arr[k].isSorted = true
            val stepMessage = "Maradék bal oldali elem másolása: ${arr[k].value}"
            _mergeSortSteps.postValue(stepMessage)
            i++
            k++
        }

        while (j < rightArray.size) {
            arr[k] = rightArray[j].copy()
            arr[k].isCurrentlyCompared = false
            arr[k].isSorted = true
            val stepMessage = "Maradék jobb oldali elem másolása: ${arr[k].value}"
            _mergeSortSteps.postValue(stepMessage)
            j++
            k++
        }

        for (index in l..r) {
            arr[index].isCurrentlyCompared = false
            arr[index].isSorted = true
        }

        _listToSort.postValue(arr.toList())
    }

    fun mergeSortStep() {
        viewModelScope.launch {
            if (!mergeSortInProgress && currentStep < _listToSort.value!!.size - 1) {
                mergeSortInProgress = true
                performMergeSortStep(_listToSort.value!!.toMutableList(), 0, _listToSort.value!!.size - 1)
            }
        }
    }


    private suspend fun performMergeSortStep(arr: MutableList<ListUiItem>, l: Int, r: Int) {
        if (l < r) {
            val m = l + (r - l) / 2
            performMergeSortStep(arr, l, m)
            performMergeSortStep(arr, m + 1, r)
            merge(arr, l, m, r)
            _currentStepLiveData.postValue(++currentStep)
            mergeSortInProgress = false
        }
    }



    fun shuffleList() {
        val list = _listToSort.value ?: return
        _listToSort.value = list.shuffled().toMutableList()
    }

    fun restartMergeSort() {
        viewModelScope.launch {
            currentSortingJob?.cancel()


            _mergeSortSteps.value = ""

            _listToSort.value = originalList.map { it.copy(isSorted = false, isCurrentlyCompared = false, color = Color.Transparent) }

            startMergeSorting()
        }
    }

}