package com.example.algorythmics.use_case

import com.example.algorythmics.retrofit.models.SortInfo
import kotlinx.coroutines.delay

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class HeapSortUseCase {

    suspend fun heapSort(list: MutableList<Int>): Flow<List<SortInfo>> = flow {
        val sortInfoList = mutableListOf<SortInfo>()
        buildMaxHeap(list, sortInfoList)

        for (i in list.size - 1 downTo 1) {
            list.swap(0, i)
            sortInfoList.add(
                SortInfo(
                    list[i],
                    shouldSwap = true,
                    hadNoEffect = false,
                    position = i
                )
            )
            emit(sortInfoList.toList())
            maxHeapify(list, 0, i, sortInfoList)
        }

        sortInfoList.add(SortInfo(list[0], shouldSwap = false, hadNoEffect = false, position = 0))
        emit(sortInfoList.toList())
    }

    private suspend fun buildMaxHeap(list: MutableList<Int>, sortInfoList: MutableList<SortInfo>) {
        val n = list.size
        for (i in n / 2 - 1 downTo 0) {
            maxHeapify(list, i, n, sortInfoList)
        }
    }

    private suspend fun maxHeapify(
        list: MutableList<Int>,
        i: Int,
        heapSize: Int,
        sortInfoList: MutableList<SortInfo>
    ) {
        var largest = i
        val left = 2 * i + 1
        val right = 2 * i + 2

        sortInfoList.add(
            SortInfo(
                list[i],
                shouldSwap = false,
                hadNoEffect = false,
                isBeingCompared = true,
                position = i
            )
        )
        if (left < heapSize && list[left] > list[largest]) {
            largest = left
        }

        if (right < heapSize && list[right] > list[largest]) {
            largest = right
        }

        if (largest != i) {
            list.swap(i, largest)
            sortInfoList.add(
                SortInfo(
                    list[i],
                    shouldSwap = true,
                    hadNoEffect = false,
                    position = i
                )
            )
            maxHeapify(list, largest, heapSize, sortInfoList)
        } else {
            sortInfoList.add(
                SortInfo(
                    list[i],
                    shouldSwap = false,
                    hadNoEffect = true,
                    position = i
                )
            )
        }
    }

    private suspend fun MutableList<Int>.swap(i: Int, j: Int) {
        val temp = this[i]
        this[i] = this[j]
        this[j] = temp
    }
}