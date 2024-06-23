package com.example.algorythmics.use_case

import com.example.algorythmics.retrofit.models.SelectionSortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SelectionSortUseCase {
    operator fun invoke(list: MutableList<Int>): Flow<SelectionSortInfo> = flow {

        val n = list.size

        for (i in 0 until n - 1) {
            var minIndex = i

            for (j in i + 1 until n) {
                emit(SelectionSortInfo(currentItem = j, shouldSwap = false, itemToSwap = minIndex, hadNoEffect = false))
                delay(500)
                if (list[j] < list[minIndex]) {
                    minIndex = j
                }
            }

            if (minIndex != i) {
                list.swap(i, minIndex)
                emit(SelectionSortInfo(currentItem = i, shouldSwap = true, itemToSwap = minIndex, hadNoEffect = false))
                delay(500)
            } else {
                emit(SelectionSortInfo(currentItem = i, shouldSwap = false, itemToSwap = minIndex, hadNoEffect = true))
                delay(500)
            }
        }
    }
}