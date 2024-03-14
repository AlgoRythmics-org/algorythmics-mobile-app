package com.example.algorythmics.presentation

data class ListUiItem(
    val id:Int,
    val value:Int,
    var isCurrentlyCompared: Boolean = false,
    var isSwap: Boolean = false,
    var isInitialColorNeeded: Boolean = true,
    var isSorted: Boolean = false,
    var isFound: Boolean = false,
    val needsColorUpdate: Boolean = false

)
