package com.example.algorythmics.retrofit.models

data class MergeSortInfo(
    val currentItemIndex: Int,
    val swappedItemIndex: Int,
    val isSwap: Boolean,
    val isFinal: Boolean,
    val swapItemColor: Int? = null
)
