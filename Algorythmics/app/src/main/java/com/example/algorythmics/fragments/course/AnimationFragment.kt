
package com.example.algorythmics.fragments.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.presentation.BinarySearchViewModel
import com.example.algorythmics.presentation.HeapSortViewModel
import com.example.algorythmics.presentation.InsertionSortViewModel
import com.example.algorythmics.presentation.ItemDecoration
import com.example.algorythmics.presentation.LinearSearchViewModel
import com.example.algorythmics.presentation.MergeSortViewModel
import com.example.algorythmics.presentation.QuickSortViewModel
import com.example.algorythmics.presentation.SelectionSortViewModel
import com.example.algorythmics.presentation.ShellSortViewModel
import com.example.algorythmics.presentation.SortViewModel




class AnimationFragment : Fragment() {

    private val sortViewModel: SortViewModel by activityViewModels()
    private val insertionSortViewModel: InsertionSortViewModel by activityViewModels()
    private val selectionSortViewModel: SelectionSortViewModel by activityViewModels()
    private val shellSortViewModel: ShellSortViewModel by activityViewModels()
    private val quickSortViewModel: QuickSortViewModel by activityViewModels()
    private val linearSearchViewModel: LinearSearchViewModel by activityViewModels()
    private val binarySearchViewModel: BinarySearchViewModel by activityViewModels()
    private val mergeSortViewModel: MergeSortViewModel by activityViewModels()
    private val heapSortViewModel: HeapSortViewModel by activityViewModels()
    private lateinit var btnSortList: Button
    private lateinit var secondButton: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var sortedListAdapter: SortedListAdapter
    private lateinit var selectionSortListAdapter: SelectionSortListAdapter
    private lateinit var mergeSortAdapter: MergeSortAdapter
    private lateinit var binarySearchAdapter: BinarySearchAdapter
    private lateinit var algorithmId: String
    private lateinit var etSearchNumber: EditText

    private lateinit var tvAnimationSteps: TextView


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val itemDecoration = ItemDecoration()

        // Az algoritmus azonosítójának beállítása az előző fragmentből kapott adat alapján
        algorithmId = arguments?.getString("algorithmId") ?: ""

        btnSortList = view.findViewById(R.id.btn_sort_list)
        secondButton = view.findViewById(R.id.second_button)
        recyclerView = view.findViewById(R.id.rv_container)
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.addItemDecoration(itemDecoration)
        etSearchNumber = view.findViewById(R.id.et_search_number)

        tvAnimationSteps = view.findViewById(R.id.tv_scrollable)


        sortedListAdapter = SortedListAdapter()
        selectionSortListAdapter = SelectionSortListAdapter()
        mergeSortAdapter = MergeSortAdapter()
        binarySearchAdapter = BinarySearchAdapter()
        recyclerView.adapter = when (algorithmId) {
            "653d35f6ce1b18cbd8bd14b3" -> selectionSortListAdapter
            "653d3dfece1b18cbd8bd14b9" -> binarySearchAdapter
            "653d36ecce1b18cbd8bd14b4" -> mergeSortAdapter
            else -> sortedListAdapter
        }



        // Gomb eseménykezelője
        btnSortList.setOnClickListener {
            // Az algoritmus azonosítójának alapján döntsön, hogy melyik ViewModel-et használja
            when (algorithmId) {
                "65b8db1995d5f3a10bccd361" -> sortViewModel.startSorting()
                "653d32ffce1b18cbd8bd14b2" -> insertionSortViewModel.startInsertionSorting()
                "653d35f6ce1b18cbd8bd14b3" -> selectionSortViewModel.startSelectionSorting()
                "653d3c49ce1b18cbd8bd14b7" -> shellSortViewModel.startShellSorting()
                "653d3aa2ce1b18cbd8bd14b5" -> quickSortViewModel.startQuickSorting()
                "653d36ecce1b18cbd8bd14b4" -> mergeSortViewModel.startMergeSorting()
                "653d3b95ce1b18cbd8bd14b6" -> heapSortViewModel.startHeapSorting()
                "653d3cf5ce1b18cbd8bd14b8" -> {
                    val searchNumber = etSearchNumber.text.toString().toIntOrNull() ?: return@setOnClickListener
                    linearSearchViewModel.startLinearSearch(searchNumber)
                }
                "653d3dfece1b18cbd8bd14b9" -> {
                    val searchNumber2 = etSearchNumber.text.toString().toIntOrNull() ?: return@setOnClickListener
                    binarySearchViewModel.startBinarySearch(searchNumber2)
                }
            }
        }
        secondButton.setOnClickListener {
            when (algorithmId) {
                "65b8db1995d5f3a10bccd361" -> sortViewModel.stepSorting()
            }
        }

        // LiveData megfigyelése a RecyclerView frissítéséhez
        sortViewModel.listToSort.observe(viewLifecycleOwner, Observer { list ->
            sortedListAdapter.submitList(list)
        })

        sortViewModel.comparisonMessage.observe(viewLifecycleOwner, Observer { message ->
            tvAnimationSteps.text = tvAnimationSteps.text.toString() + "\n" + message
        })


        insertionSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        selectionSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            selectionSortListAdapter.submitList(it)
        })

        shellSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        quickSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        mergeSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            mergeSortAdapter.submitList(it)
        })

        heapSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })


        linearSearchViewModel.listToSearch.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })

        binarySearchViewModel.listToSearch.observe(viewLifecycleOwner, Observer {
            binarySearchAdapter.submitList(it)
        })

        linearSearchViewModel.searchResult.observe(viewLifecycleOwner, Observer { result ->
            // Ha van keresési eredmény, frissítjük a listát és jelöljük a találatot
            result?.let {
                val newList = sortedListAdapter.currentList.mapIndexed { index, item ->
                    item.copy(isCurrentlyCompared = index == result)
                }
                sortedListAdapter.submitList(newList)
                val message = "A keresett szám megtalálva a(z) ${result + 1}. helyen."
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        binarySearchViewModel.searchResult.observe(viewLifecycleOwner, Observer { result ->
            // Ha van keresési eredmény, frissítjük a listát és jelöljük a találatot
            result?.let {
                val newList = sortedListAdapter.currentList.mapIndexed { index, item ->
                    item.copy(isCurrentlyCompared = index == result)
                }
                sortedListAdapter.submitList(newList)
                val message = "A keresett szám megtalálva a(z) ${result + 1}. helyen."
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        })

        // Ha az algoritmus azonosítója "653d3cf5ce1b18cbd8bd14b8", akkor mutassuk meg az EditText-et
        if (algorithmId == "653d3cf5ce1b18cbd8bd14b8" || algorithmId == "653d3dfece1b18cbd8bd14b9") {
            etSearchNumber.visibility = View.VISIBLE
        } else {
            etSearchNumber.visibility = View.GONE
        }
    }
}
