package com.example.algorythmics.use_case

import com.example.algorythmics.retrofit.models.SortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.collect

class MergeSortUseCase {
    operator fun invoke(
        list: MutableList<Int>,
        start: Int = 0,
        end: Int = list.size - 1
    ): Flow<SortInfo> = flow {
        if (start < end) {
            val mid = (start + end) / 2
            invoke(list, start, mid).collect { emit(it) }
            invoke(list, mid + 1, end).collect { emit(it) }
            merge(list, start, mid, end)
        }
    }

    private suspend fun FlowCollector<SortInfo>.merge(
        list: MutableList<Int>,
        start: Int,
        mid: Int,
        end: Int
    ) {
        val leftSize = mid - start + 1
        val rightSize = end - mid

        val leftArray = IntArray(leftSize)
        val rightArray = IntArray(rightSize)

        for (i in 0 until leftSize)
            leftArray[i] = list[start + i]

        for (j in 0 until rightSize)
            rightArray[j] = list[mid + 1 + j]

        var i = 0
        var j = 0
        var k = start

        while (i < leftSize && j < rightSize) {
            if (leftArray[i] <= rightArray[j]) {
                list[k] = leftArray[i]
                i++
            } else {
                list[k] = rightArray[j]
                j++
            }
            emit(SortInfo(currentItem = k, shouldSwap = false, hadNoEffect = false))
            delay(500)
            k++
        }

        while (i < leftSize) {
            list[k] = leftArray[i]
            emit(SortInfo(currentItem = k, shouldSwap = false, hadNoEffect = false))
            delay(500)
            i++
            k++
        }

        while (j < rightSize) {
            list[k] = rightArray[j]
            emit(SortInfo(currentItem = k, shouldSwap = false, hadNoEffect = false))
            delay(500)
            j++
            k++
        }
    }
}