package com.example.algorythmics.presentation
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.animation.SortingViewModel
import com.example.algorythmics.use_case.BubbleSortUseCase
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class BubbleSortViewModel(
    private val bubbleSortUseCase: BubbleSortUseCase = BubbleSortUseCase(),
) : SortingViewModel() {

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    private val _animationSteps = MutableLiveData<String>()
    val animationSteps: LiveData<String> get() = _animationSteps

    private val _comparisonMessage = MutableLiveData<String>()
    val comparisonMessage: LiveData<String> get() = _comparisonMessage

    init {
        val list = mutableListOf<ListUiItem>()
        for ((i, item) in bubbleSortUseCase.items.withIndex()) {
            list.add(ListUiItem(id = i, value = item))
        }
        _listToSort.value = list
    }

    override fun startSorting() {
        viewModelScope.launch {
            super.startSorting()
            var pass = 0
            var lastSortedIndex = _listToSort.value!!.lastIndex

            bubbleSortUseCase().collect { swapInfo ->
                val currentItemIndex = swapInfo.currentItem
                val newList = _listToSort.value!!.toMutableList()
                newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = true)
                newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = true)

                if (swapInfo.shouldSwap) {

                    val firstItem = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex + 1] = firstItem
                    //val comparisonMessage = "Összehasonlítva: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}"
                    //_comparisonMessage.postValue("$comparisonMessage\nElemek felcserélve: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}")
                } else if (swapInfo.hadNoEffect) {

                    newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                   // val comparisonMessage = "Összehasonlítva: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}"
                   // _comparisonMessage.postValue("$comparisonMessage\nElemek nem cseréltek helyet: ${newList[currentItemIndex].value} és ${newList[currentItemIndex + 1].value}")
                }

                if (currentItemIndex == lastSortedIndex) {

                    newList[currentItemIndex] = newList[currentItemIndex].copy(isSorted = true, color = Color.Gray)
                    lastSortedIndex = currentItemIndex - 1
                }

                _listToSort.value = newList

                val step = "Pass: $pass, Comparison: ${swapInfo.currentItem}, Should Swap: ${swapInfo.shouldSwap}, Had No Effect: ${swapInfo.hadNoEffect}"
                _animationSteps.postValue(step)
            }

            for (i in _listToSort.value!!.indices.reversed()) {
                val newList = _listToSort.value!!.toMutableList()
                newList[i] = newList[i].copy(isSorted = true, color = Color.Gray)
                _listToSort.postValue(newList)
                delay(500)
            }
        }
    }

    private var step = 0
    private var pass = 0
    private var currentComparisonIndex = 0

    fun stepSorting() {
        viewModelScope.launch {
            val list = _listToSort.value!!.toMutableList()
            val n = list.size

            if (pass >= n) {
                for (i in list.indices.reversed()) {
                    list[i] = list[i].copy(isSorted = true, color = Color.Gray)
                    _listToSort.value = list.toList()

                }
                return@launch
            }

            when (step) {
                0 -> {
                    if (currentComparisonIndex < n - pass - 1) {

                        list[currentComparisonIndex] = list[currentComparisonIndex].copy(isCurrentlyCompared = true)
                        list[currentComparisonIndex + 1] = list[currentComparisonIndex + 1].copy(isCurrentlyCompared = true)
                        _listToSort.value = list.toList()


                        if (list[currentComparisonIndex].value > list[currentComparisonIndex + 1].value) {
                            step = 1
                        } else {
                            step = 2
                        }
                    } else {
                        list[n - pass - 1] = list[n - pass - 1].copy(isSorted = true, color = Color.Gray)
                        _listToSort.value = list.toList()
                        pass++
                        currentComparisonIndex = 0
                        step = 0
                    }
                }
                1 -> {

                    val temp = list[currentComparisonIndex]
                    list[currentComparisonIndex] = list[currentComparisonIndex + 1]
                    list[currentComparisonIndex + 1] = temp
                    _listToSort.value = list.toList()



                    list[currentComparisonIndex] = list[currentComparisonIndex].copy(isCurrentlyCompared = false)
                    list[currentComparisonIndex + 1] = list[currentComparisonIndex + 1].copy(isCurrentlyCompared = false)
                    _listToSort.value = list.toList()

                    step = 2
                }
                2 -> {

                    list[currentComparisonIndex] = list[currentComparisonIndex].copy(isCurrentlyCompared = false)
                    list[currentComparisonIndex + 1] = list[currentComparisonIndex + 1].copy(isCurrentlyCompared = false)
                    _listToSort.value = list.toList()

                    currentComparisonIndex++
                    step = 0
                }
            }
        }
    }



    fun shuffleList() {
        val list = _listToSort.value ?: return
        val shuffledList = list.shuffled()
        _listToSort.value = shuffledList.toMutableList()
    }

}
