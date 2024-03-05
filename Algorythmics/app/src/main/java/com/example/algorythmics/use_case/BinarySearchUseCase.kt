package com.example.algorythmics.use_case

import com.example.algorythmics.presentation.ListUiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BinarySearchUseCase {
    operator fun invoke(list: MutableList<Int>, searchNumber: Int): Flow<Int?> = flow {
        var low = 0
        var high = list.size - 1
        var foundIndex: Int? = null

        while (low <= high) {
            val mid = (low + high) / 2
            val guess = list[mid]

            if (guess == searchNumber) {
                foundIndex = mid
                break
            }
            if (guess < searchNumber) {
                low = mid + 1
            } else {
                high = mid - 1
            }
            // Emit progress if needed
            emit(foundIndex)
        }

        emit(foundIndex)
    }

}