package com.example.algorythmics.fragments.course

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.presentation.InsertionSortViewModel
import com.example.algorythmics.presentation.SelectionSortViewModel
import com.example.algorythmics.presentation.ShellSortViewModel
import com.example.algorythmics.presentation.SortViewModel


class AnimationFragment : Fragment() {

    private val sortViewModel: SortViewModel by activityViewModels()
    private val insertionSortViewModel: InsertionSortViewModel by activityViewModels()
    private val selectionSortViewModel: SelectionSortViewModel by activityViewModels()
    private val shellSortViewModel: ShellSortViewModel by activityViewModels()
    private lateinit var btnSortList: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var sortedListAdapter: SortedListAdapter
    private lateinit var selectionSortListAdapter: SelectionSortListAdapter
    private var algorithmId: String? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Az algoritmus azonosítójának beállítása az előző fragmentből kapott adat alapján
        algorithmId = arguments?.getString("algorithmId")

        btnSortList = view.findViewById(R.id.btn_sort_list)
        recyclerView = view.findViewById(R.id.rv_container)

        sortedListAdapter = SortedListAdapter()
        selectionSortListAdapter = SelectionSortListAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            //adapter = sortedListAdapter
            adapter = if (algorithmId == "653d35f6ce1b18cbd8bd14b3") selectionSortListAdapter else sortedListAdapter
        }

        // Gomb eseménykezelője
        btnSortList.setOnClickListener {
            // Az algoritmus azonosítójának alapján döntsön, hogy melyik ViewModel-et használja
            when (algorithmId) {
                "65b8db1995d5f3a10bccd361" -> sortViewModel.startSorting()
                "653d32ffce1b18cbd8bd14b2" -> insertionSortViewModel.startInsertionSorting()
                "653d35f6ce1b18cbd8bd14b3" -> selectionSortViewModel.startSelectionSorting()
                "653d3c49ce1b18cbd8bd14b7" -> shellSortViewModel.startShellSorting()
            }
        }

        // LiveData megfigyelése a RecyclerView frissítéséhez
        sortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
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
    }
}