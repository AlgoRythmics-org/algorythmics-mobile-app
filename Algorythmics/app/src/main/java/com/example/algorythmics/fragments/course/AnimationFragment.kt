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
import com.example.algorythmics.presentation.SortViewModel


class AnimationFragment : Fragment() {


    private val sortViewModel: SortViewModel by activityViewModels()
    private val insertionSortViewModel: InsertionSortViewModel by activityViewModels()
    private lateinit var btnSortList: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var sortedListAdapter: SortedListAdapter


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSortList = view.findViewById(R.id.btn_sort_list)
        recyclerView = view.findViewById(R.id.rv_container)



        btnSortList.setOnClickListener {
            insertionSortViewModel.startSorting()
        }

        sortedListAdapter = SortedListAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sortedListAdapter
        }

            insertionSortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })
    }
}