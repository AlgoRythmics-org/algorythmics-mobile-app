package com.example.algorythmics.use_case

import com.example.algorythmics.retrofit.models.QuickSortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow


class QuickSortUseCase {
    operator fun invoke(list: MutableList<Int>, low: Int, high: Int): Flow<List<QuickSortInfo>> = flow {
        quickSort(list, low, high, mutableListOf()) // Start the quick sort process
    }

    private suspend fun FlowCollector<List<QuickSortInfo>>.quickSort(list: MutableList<Int>, low: Int, high: Int, process: MutableList<QuickSortInfo>) {
        if (low < high) {
            val partitionIndex = partition(list, low, high)
            process.add(QuickSortInfo(partitionIndex, list.toList())) // Add current state after partitioning
            delay(500)
            emit(process.toList()) // Emit the current process state
            val leftProcess = mutableListOf<QuickSortInfo>()
            quickSort(list, low, partitionIndex - 1, leftProcess) // Sort left partition
            val rightProcess = mutableListOf<QuickSortInfo>()
            quickSort(list, partitionIndex + 1, high, rightProcess) // Sort right partition
        }
    }

    private fun partition(list: MutableList<Int>, low: Int, high: Int): Int {
        val pivot = list[high]
        var i = low - 1

        for (j in low until high) {
            if (list[j] < pivot) {
                i++
                list.swap(i, j)
            }
        }
        list.swap(i + 1, high)
        return i + 1
    }

    private fun MutableList<Int>.swap(i: Int, j: Int) {
        val temp = this[i]
        this[i] = this[j]
        this[j] = temp
    }
}