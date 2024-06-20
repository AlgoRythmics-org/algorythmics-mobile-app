package com.example.algorythmics.presentation

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.google.android.filament.Box

data class ListUiItem(
    val id:Int,
    val value:Int,
    var isCurrentlyCompared: Boolean = false,
    var isSwap: Boolean = false,
    var isInitialColorNeeded: Boolean = true,
    var isSorted: Boolean = false,
    var isSortedPosition: Boolean = false,
    var isFound: Boolean = false,
    val needsColorUpdate: Boolean = false,
    var shouldMove: Boolean = false,
    val color: Color = Color.Blue, // Default color
    val animatedValue: Float = value.toFloat(),
    var xPosition: Double = 0.0,
    var yPosition: Double = 0.0,

)
