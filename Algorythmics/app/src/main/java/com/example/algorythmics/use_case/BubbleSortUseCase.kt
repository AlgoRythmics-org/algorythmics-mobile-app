package com.example.algorythmics.use_case

import com.example.algorythmics.animation.SortingAlgorithm
import com.example.algorythmics.retrofit.models.SortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BubbleSortUseCase : SortingAlgorithm() {

    private var currentItemIndex = 0
    private var pass = 0
    init {
        initialize()
    }


    operator fun invoke() : Flow<SortInfo> = flow {
        val n = elements.size
        for (i in 0 until n - 1) {
            for (j in 0 until n - i - 1) {
                emit(SortInfo(currentItem = j, shouldSwap = false, hadNoEffect = false))
                delay(800)
                if (compare(j, j+1) > 0) {
                    swap(j, j + 1)
                    emit(SortInfo(currentItem = j, shouldSwap = true, hadNoEffect = false))
                } else {
                    emit(SortInfo(currentItem = j, shouldSwap = false, hadNoEffect = true))
                }
                delay(800)
            }
        }
    }



}

fun <T> MutableList<T>.swap(indexOne:Int, indexTwo:Int){
    val tempOne = this[indexOne]
    this[indexOne] = this[indexTwo]
    this[indexTwo] = tempOne
}