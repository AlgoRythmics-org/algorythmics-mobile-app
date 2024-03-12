package com.example.algorythmics.use_case

import androidx.compose.ui.graphics.Color
import com.example.algorythmics.retrofit.models.MergeSortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow


class MergeSortUseCase {

    private val _mergeSortInfo = MutableSharedFlow<MergeSortInfo>()
    suspend fun mergeSort(list: MutableList<Int>, startIndex: Int, endIndex: Int): Flow<List<Int>> = flow {
        if (startIndex < endIndex) {
            val mid = (startIndex + endIndex) / 2
            mergeSort(list, startIndex, mid).collect { emit(it) }
            mergeSort(list, mid + 1, endIndex).collect { emit(it) }
            merge(list, startIndex, mid, endIndex).collect { emit(it) }
        } else {
            emit(list)
        }
    }

    private suspend fun merge(list: MutableList<Int>, left: Int, mid: Int, right: Int): Flow<List<Int>> = flow {
        val temp = mutableListOf<Int>()
        var i = left
        var j = mid + 1
        while (i <= mid && j <= right) {
            temp.add(
                if (list[i] <= list[j]) {
                    val copy = list[i]
                    i++
                    copy
                } else {
                    val copy = list[j]
                    j++
                    copy
                }
            )
            delay(500)
            emit(list.toList())
        }
        while (i <= mid) {
            temp.add(list[i++])
            emit(list.toList())
        }
        while (j <= right) {
            temp.add(list[j++])
            emit(list.toList())
        }
        for (k in left..right) {
            list[k] = temp[k - left]
            // Jelöljük meg azokat az elemeket, amelyeket cserélünk
            _mergeSortInfo.emit(MergeSortInfo(k, k - left, true, false))
            emit(list.toList())
            delay(500)
        }
        emit(list.toList())
    }



}