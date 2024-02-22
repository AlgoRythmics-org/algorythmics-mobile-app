package com.example.algorythmics.use_case

import com.example.algorythmics.retrofit.models.SortInfo
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class BubbleSortUseCase {

    operator fun invoke(list:MutableList<Int>) : Flow<SortInfo> = flow{

        var listSizeToCompare = list.size-1
        while(listSizeToCompare>1){
            var innerIterator = 0
            while(innerIterator<listSizeToCompare){
                val currentListItem = list[innerIterator]
                val nextListItem = list[innerIterator+1]
                emit(
                    SortInfo(currentItem = innerIterator, shouldSwap = false, hadNoEffect = false)
                )
                delay(500)
                if(currentListItem > nextListItem){
                    list.swap(innerIterator,innerIterator+1)
                    emit(
                        SortInfo(currentItem = innerIterator, shouldSwap = true, hadNoEffect = false)
                    )
                }else{
                    emit(
                        SortInfo(currentItem = innerIterator, shouldSwap = false, hadNoEffect = true)
                    )
                }
                delay(500)
                innerIterator +=1
            }
            listSizeToCompare -= 1
        }
    }
}

fun <T> MutableList<T>.swap(indexOne:Int, indexTwo:Int){
    val tempOne = this[indexOne]
    this[indexOne] = this[indexTwo]
    this[indexTwo] = tempOne
}