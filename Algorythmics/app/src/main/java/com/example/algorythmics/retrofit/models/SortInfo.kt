package com.example.algorythmics.retrofit.models

data class SortInfo(
    val currentItem:Int,
    val shouldSwap:Boolean,
    val hadNoEffect:Boolean,
    val isBeingCompared: Boolean = false,
    val position: Int = 0
)
