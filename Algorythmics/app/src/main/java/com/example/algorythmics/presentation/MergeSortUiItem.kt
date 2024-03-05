package com.example.algorythmics.presentation

import androidx.compose.ui.graphics.Color
import com.example.algorythmics.retrofit.models.SortState

data class MergeSortUiItem(
    val id:String,
    val depth:Int,
    val sortState: SortState,
    val sortParts:List<List<Int>>,
    val color: Color
)
