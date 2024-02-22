package com.example.algorythmics.fragments.course


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.presentation.ListUiItem
import com.example.algorythmics.presentation.SortViewModel

class AnimationFragment : Fragment() {


    private lateinit var sortViewModel: SortViewModel
    private lateinit var btnSortList: Button
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_animation, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sortViewModel = SortViewModel()

        btnSortList = view.findViewById(R.id.btn_sort_list)
        recyclerView = view.findViewById(R.id.rv_sorted_list)

        btnSortList.setOnClickListener {
            sortViewModel.startSorting()
        }

        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = SortedListAdapter(sortViewModel.listToSort)
        }
    }
}