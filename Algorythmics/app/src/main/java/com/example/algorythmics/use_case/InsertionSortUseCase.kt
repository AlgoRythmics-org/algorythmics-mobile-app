package com.example.algorythmics.use_case

import com.example.algorythmics.retrofit.models.SortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class InsertionSortUseCase {
    operator fun invoke(list: MutableList<Int>): Flow<SortInfo> = flow {
        for (i in 1 until list.size) {
            val key = list[i]
            var j = i - 1

            // Csak az aktuális elemet jelöljük pirosnak
            emit(SortInfo(currentItem = i, shouldSwap = false, hadNoEffect = true))
            delay(500)

            while (j >= 0 && list[j] > key) {
                // Csak az aktuális elemet és a vele összehasonlított elemet jelöljük pirosnak
                emit(SortInfo(currentItem = j, shouldSwap = true, hadNoEffect = false))
                delay(500)
                list[j + 1] = list[j]
                j--
            }

            list[j + 1] = key

            // A rendezett elemek visszaállítása szürkére
            for (k in 0 until i + 1) {
                emit(SortInfo(currentItem = k, shouldSwap = false, hadNoEffect = true))
                delay(500)
            }
        }
    }
}