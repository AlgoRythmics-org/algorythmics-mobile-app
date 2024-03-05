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

    private val _listToSearch = MutableLiveData<MutableList<Int>>()
    //val listToSearch: LiveData<MutableList<Int>> get() = _listToSearch

    private val _searchResult = MutableLiveData<Int?>()
    val searchResult: LiveData<Int?> get() = _searchResult

    private val binarySearchUseCase = BinarySearchUseCase()

    fun startBinarySearch(searchNumber: Int) {
        viewModelScope.launch {
            _searchResult.value = null
            val list = _listToSearch.value ?: return@launch

            binarySearchUseCase(list, searchNumber).collect { result ->
                _searchResult.value = result
            }
        }
    }

    init {
        val list = mutableListOf<Int>()
        for (i in 0 until 10) {
            list.add(i * 2)
        }
        list.shuffle() // Megkeverjük a számokat
        setListToSearch(list)
    }

    fun setListToSearch(list: MutableList<Int>) {
        _listToSearch.value = list
    }

    fun sortList() {
        val list = _listToSearch.value ?: return
        list.sort() // Növekvő sorrendbe rendezzük
        setListToSearch(list)
    }

}
