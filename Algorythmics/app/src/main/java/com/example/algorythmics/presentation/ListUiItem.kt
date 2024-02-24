package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color

data class ListUiItem(
    val id:Int,
    val value:Int,
    var isCurrentlyCompared: Boolean = false,
    var isInitialColorNeeded: Boolean = true,
    var isSorted: Boolean = false,

)
