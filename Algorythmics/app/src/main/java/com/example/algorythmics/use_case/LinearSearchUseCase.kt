package com.example.algorythmics.use_case

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class LinearSearchUseCase {
    operator fun invoke(list: MutableList<Int>, searchNumber: Int): Flow<Int?> = flow {
        var foundIndex: Int? = null
        for (i in list.indices) {
            if (list[i] == searchNumber) {
                foundIndex = i
                break
            }
        }
        emit(foundIndex)
    }
}