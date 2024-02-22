package com.example.algorythmics.fragments.course
import SortViewModel
import SortedListAdapter
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



class AnimationFragment : Fragment() {


    private val sortViewModel: SortViewModel by activityViewModels()
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
        recyclerView = view.findViewById(R.id.rv_sorted_list)



        btnSortList.setOnClickListener {
            sortViewModel.startSorting()
        }

        sortedListAdapter = SortedListAdapter()
        recyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = sortedListAdapter
        }

        sortViewModel.listToSort.observe(viewLifecycleOwner, Observer {
            sortedListAdapter.submitList(it)
        })
    }
}