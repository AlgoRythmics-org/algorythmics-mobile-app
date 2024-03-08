package com.example.algorythmics.use_case

import android.util.Log
import com.example.algorythmics.retrofit.models.SortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow

class HeapSortUseCase {
    suspend operator fun invoke(list: MutableList<Int>): Flow<SortInfo> = flow {
        buildHeap(list)

        val n = list.size

        for (i in n - 1 downTo 0) {
            swap(list, i, 0)
            emit(SortInfo(currentItem = i, shouldSwap = true, hadNoEffect = false))
            delay(500)
            heapify(list, i, 0)
        }
    }

    private fun buildHeap(arr: MutableList<Int>) {
        val n = arr.size

        for (i in n / 2 - 1 downTo 0) {
            heapify(arr, n, i)
        }
    }

    private fun heapify(arr: MutableList<Int>, n: Int, i: Int) {
        var largest = i
        val l = 2 * i + 1
        val r = 2 * i + 2

        if (l < n && arr[l] > arr[largest]) {
            largest = l
        }

        if (r < n && arr[r] > arr[largest]) {
            largest = r
        }

        if (largest != i) {
            swap(arr, i, largest)
            heapify(arr, n, largest)
        }
    }

    private fun swap(arr: MutableList<Int>, x: Int, y: Int) {
        val temp = arr[x]
        arr[x] = arr[y]
        arr[y] = temp
    }
}