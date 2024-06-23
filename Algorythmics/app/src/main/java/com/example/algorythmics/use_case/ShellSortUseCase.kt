package com.example.algorythmics.use_case

import com.example.algorythmics.retrofit.models.ShellSortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ShellSortUseCase {
    fun sort(list: MutableList<Int>): Flow<ShellSortInfo> = flow {
        val n = list.size
        var gap = n / 2

        while (gap > 0) {
            for (i in gap until n) {
                val temp = list[i]
                var j = i

                while (j >= gap && list[j - gap] > temp) {
                    list[j] = list[j - gap]
                    emit(ShellSortInfo(gap = gap, currentItem = j, itemToSwap = j - gap))
                    delay(1000)
                    j -= gap
                }

                list[j] = temp
                emit(ShellSortInfo(gap = gap, currentItem = j, itemToSwap = -1))
                delay(1000)
            }
            gap /= 2
        }
    }
}