package com.example.algorythmics.animation

interface SortingSteps {
    fun sort()
    fun compare(index1: Int, index2: Int): Int
    fun swap(index1: Int, index2: Int)
}