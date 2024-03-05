package com.example.algorythmics.retrofit.models

data class MergeSortInfo(
    val id:String,
    val depth:Int,
    val sortParts:List<Int>,
    val sortState:SortState
)



enum class SortState(val value:Int){
    DIVIDED(0),
    MERGED(1)
}