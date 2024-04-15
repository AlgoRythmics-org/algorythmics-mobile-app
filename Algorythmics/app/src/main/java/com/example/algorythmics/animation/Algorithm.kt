package com.example.algorythmics.animation

import kotlin.random.Random

open class Algorithm {

    protected val elements: MutableList<Int> = mutableListOf()
    val items : List<Int> = elements

    protected val code : StringBuilder = StringBuilder()

    companion object {
        val inputCode = """
            int i, j;
            bool swapped;
            for (i = 0; i < n - 1; i++) {
                swapped = false;
                for (j = 0; j < n - i - 1; j++) {
                    if (arr[j] > arr[j + 1]) {
                        swap(arr[j], arr[j + 1]);
                        swapped = true;
                    }
                }
                if (swapped == false)
                    break;
            }
        """.trimIndent()

    }

    // Inicializáló függvény, amely feltölti a listát véletlenszerű elemekkel
    open fun initialize() {
        elements.clear() // Töröljük az esetleges korábbi elemeket
        repeat(10) {
            elements.add(Random.nextInt(150)) // Véletlenszerű számokat generálunk és hozzáadjuk a listához
        }
    }
}