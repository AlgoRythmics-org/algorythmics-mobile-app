package com.example.algorythmics.presentation


import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.use_case.HeapSortUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class HeapSortViewModel(private val heapSortUseCase: HeapSortUseCase = HeapSortUseCase()) : ViewModel() {

    private var originalList = mutableListOf<ListUiItem>()
    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> get() = _comparisonMessage

    private var currentSortingJob: Job? = null

    private var heapSize = 0
    private var currentComparisonIndex = 0

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
                    value = (1..150).random()
                )
            )
        }
        _listToSort.value = list
        heapSize = list.size
        originalList = list
    }

    fun startHeapSorting() {
        currentSortingJob?.cancel()
        currentSortingJob = viewModelScope.launch {
            val list = _listToSort.value!!.toMutableList()
            var sortingFinished = false

            while (!sortingFinished) {
                if (heapSize <= 1) {
                    list.forEachIndexed { index, item ->
                        list[index] = item.copy(isSorted = true)
                    }
                    _listToSort.value = list
                    _comparisonMessage.value = "Rendezés befejeződött!"
                    sortingFinished = true
                } else {
                    if (currentComparisonIndex == heapSize - 1) {
                        currentComparisonIndex = 0
                    }

                    if (currentComparisonIndex == 0) {
                        buildMaxHeap(list)
                        _comparisonMessage.value = "Maximum kupac építése"
                    }

                    list[0] = list[0].copy(isCurrentlyCompared = true)
                    list[heapSize - 1] = list[heapSize - 1].copy(isCurrentlyCompared = true)
                    _listToSort.value = list
                    _comparisonMessage.value = "Összehasonlítás: ${list[0].value} és ${list[heapSize - 1].value}"

                    delay(800)

                    val temp = list[0]
                    list[0] = list[heapSize - 1]
                    list[heapSize - 1] = temp

                    _comparisonMessage.value = "Csere: ${list[0].value} és ${list[heapSize - 1].value}"

                    list[0] = list[0].copy(isCurrentlyCompared = false)
                    list[heapSize - 1] = list[heapSize - 1].copy(isCurrentlyCompared = false)
                    list[heapSize - 1] = list[heapSize - 1].copy(isSorted = true)
                    _listToSort.value = list

                    heapSize--
                    heapify(list, 0)

                    currentComparisonIndex++
                }
            }
        }
    }



    fun shuffleList() {
        val list = _listToSort.value ?: return
        val shuffledList = list.shuffled()
        _listToSort.value = shuffledList.toMutableList()
    }

    fun stepHeapSort() {
        viewModelScope.launch {
            val list = _listToSort.value!!.toMutableList()

            if (heapSize <= 1) {
                list.forEachIndexed { index, item ->
                    list[index] = item.copy(isSorted = true)
                }
                _listToSort.value = list
                return@launch
            }

            if (currentComparisonIndex == heapSize - 1) {
                currentComparisonIndex = 0
            }

            if (currentComparisonIndex == 0) {
                buildMaxHeap(list)
            }

            list[0] = list[0].copy(isCurrentlyCompared = true)
            list[heapSize - 1] = list[heapSize - 1].copy(isCurrentlyCompared = true)
            _listToSort.value = list

            delay(800)

            val temp = list[0]
            list[0] = list[heapSize - 1]
            list[heapSize - 1] = temp

            list[0] = list[0].copy(isCurrentlyCompared = false)
            list[heapSize - 1] = list[heapSize - 1].copy(isCurrentlyCompared = false)
            list[heapSize - 1] = list[heapSize - 1].copy(isSorted = true)
            _listToSort.value = list

            heapSize--
            heapify(list, 0)

            currentComparisonIndex++
        }
    }

    private fun buildMaxHeap(list: MutableList<ListUiItem>) {
        for (i in list.size / 2 - 1 downTo 0) {
            heapify(list, i)
        }
    }

    private fun heapify(list: MutableList<ListUiItem>, rootIndex: Int) {
        var largest = rootIndex
        val leftChild = 2 * rootIndex + 1
        val rightChild = 2 * rootIndex + 2

        if (leftChild < heapSize && list[leftChild].value > list[largest].value) {
            largest = leftChild
        }
        if (rightChild < heapSize && list[rightChild].value > list[largest].value) {
            largest = rightChild
        }

        if (largest != rootIndex) {
            val swap = list[rootIndex]
            list[rootIndex] = list[largest]
            list[largest] = swap

            _listToSort.value = list

            heapify(list, largest)
        }
    }

    fun restartHeapSort() {
        currentSortingJob?.cancel()
        viewModelScope.launch {
            _animationSteps.value = ""

            val initialList = originalList.map { it.copy(isSorted = false, isCurrentlyCompared = false) }
            _listToSort.value = initialList
            _comparisonMessage.value = ""
            heapSize = initialList.size
            currentComparisonIndex = 0

            startHeapSorting()
        }
    }



}