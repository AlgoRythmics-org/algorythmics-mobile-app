package com.example.algorythmics.animation

import com.example.algorythmics.use_case.swap
import kotlin.random.Random

open class SortingAlgorithm : SortingSteps, Algorithm(){
    override fun sort() {
        TODO("Not yet implemented")
    }

    override fun compare(index1: Int, index2: Int): Int {
       return elements[index1] - elements[index2]
    }

    override fun swap(index1: Int, index2: Int) {
        elements.swap(index1, index2)
    }
}