package com.example.algorythmics.animation

import android.util.Log

class BubbleSort : SortingAlgorithm(), SortingSteps {

    init {
        initialize()
        code.append(inputCode)
        Log.d("BubbleSort", code.toString())


    }

    override fun sort(){
       // initialize()
        Log.d("BubbleSort", "Eredeti lista: $elements")
        val n = elements.size
        for (i in 0 until n - 1) {
            var swapped = false
            for (j in 0 until n - i - 1) {
                Log.d("BubbleSort", "Összehasonlítás: ${elements[j]} és ${elements[j + 1]}")
                if (compare(elements[j], elements[j + 1]) > 0) {
                    swap(j, j + 1)
                    swapped = true
                    Log.d("BubbleSort", "Felcserélve: ${elements[j]} és ${elements[j + 1]}")
                }
            }
        }
        Log.d("BubbleSort", "Rendezett lista: $elements")
    }



    override fun compare(element1: Int, element2: Int): Int {
        return if (element1 < element2) {
            -1
        } else if (element1 > element2) {
            1
        } else {
            0
        }
    }


    override fun swap(index1: Int, index2: Int) {
        val temp = elements[index1]
        elements[index1] = elements[index2]
        elements[index2] = temp

    }
}