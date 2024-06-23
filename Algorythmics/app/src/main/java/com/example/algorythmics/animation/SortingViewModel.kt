package com.example.algorythmics.animation

import android.util.Log
import androidx.lifecycle.ViewModel

open class SortingViewModel : ViewModel() {

    open fun startSorting(){
        Log.d("Sort","Start sorting")
    }
}