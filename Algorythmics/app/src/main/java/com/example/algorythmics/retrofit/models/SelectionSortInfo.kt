package com.example.algorythmics.retrofit.models

data class SelectionSortInfo(
    val currentItem:Int,
    val shouldSwap:Boolean,
    val itemToSwap: Int,
    val hadNoEffect:Boolean
)
