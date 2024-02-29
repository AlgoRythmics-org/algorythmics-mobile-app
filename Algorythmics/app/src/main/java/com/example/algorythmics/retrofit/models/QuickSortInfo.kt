package com.example.algorythmics.retrofit.models

data class QuickSortInfo(
    val partitionIndex: Int, // Az aktuális partícióindex
    val list: List<Int> // A lista állapota a partíciózás után
)
