import androidx.compose.ui.graphics.Color
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.algorythmics.presentation.ListUiItem
import com.example.algorythmics.use_case.BubbleSortUseCase
import kotlinx.coroutines.launch
import kotlin.random.Random

class SortViewModel(private val bubbleSortUseCase: BubbleSortUseCase = BubbleSortUseCase()) : ViewModel() {

    private val _listToSort = MutableLiveData<List<ListUiItem>>()
    val listToSort: LiveData<List<ListUiItem>> get() = _listToSort

    init {
        val list = mutableListOf<ListUiItem>()
        for (i in 0 until 9) {
            val rnd = Random
            list.add(
                ListUiItem(
                    id = i,
                    isCurrentlyCompared = false,
                    value = rnd.nextInt(150),
                    color = Color(
                        255,
                        rnd.nextInt(256),
                        rnd.nextInt(256),
                        255
                    )
                )
            )
        }
        _listToSort.value = list
    }

    fun startSorting() {
        viewModelScope.launch {
            bubbleSortUseCase(_listToSort.value!!.map { it.value }.toMutableList()).collect { swapInfo ->
                val currentItemIndex = swapInfo.currentItem
                val newList = _listToSort.value!!.toMutableList()
                newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = true)
                newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = true)

                if (swapInfo.shouldSwap) {
                    val firstItem = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex + 1] = firstItem
                }
                if (swapInfo.hadNoEffect) {
                    newList[currentItemIndex] = newList[currentItemIndex].copy(isCurrentlyCompared = false)
                    newList[currentItemIndex + 1] = newList[currentItemIndex + 1].copy(isCurrentlyCompared = false)
                }
                _listToSort.value = newList
            }
        }
    }
}
