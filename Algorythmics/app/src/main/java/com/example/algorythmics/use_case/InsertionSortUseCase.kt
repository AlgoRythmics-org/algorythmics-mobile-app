package com.example.algorythmics.use_case

import com.example.algorythmics.presentation.ListUiItem
import com.example.algorythmics.retrofit.models.SortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertionSortUseCase {
    operator fun invoke(list: MutableList<Int>): Flow<SortInfo> = flow {
        for (i in 1 until list.size) {
            val key = list[i]
            var j = i - 1

            emit(SortInfo(currentItem = i, shouldSwap = false, hadNoEffect = false))
            delay(500)

            while (j >= 0 && list[j] > key) {
                list[j + 1] = list[j]
                emit(SortInfo(currentItem = j, shouldSwap = true, hadNoEffect = false))
                delay(500)
                j--
            }
            list[j + 1] = key
            emit(SortInfo(currentItem = j + 1, shouldSwap = false, hadNoEffect = false))
            delay(500)
        }
    }

}