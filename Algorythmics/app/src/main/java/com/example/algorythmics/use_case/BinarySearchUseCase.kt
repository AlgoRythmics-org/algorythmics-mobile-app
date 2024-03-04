package com.example.algorythmics.use_case

import com.example.algorythmics.presentation.ListUiItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BinarySearchUseCase {
    operator fun invoke(list: List<ListUiItem>, searchNumber: Int): Int? {
        var left = 0
        var right = list.size - 1

        while (left <= right) {
            val mid = (left + right) / 2
            val midValue = list[mid].value
            if (midValue == searchNumber) {
                return mid // Az elem megtalálva
            }
            if (midValue < searchNumber) {
                left = mid + 1 // Ha a középső elem kisebb, mint a keresett szám, a jobb oldalra kell mennünk
            } else {
                right = mid - 1 // Különben a bal oldalra kell mennünk
            }
        }
        return null // Elem nem található a listában
    }
}