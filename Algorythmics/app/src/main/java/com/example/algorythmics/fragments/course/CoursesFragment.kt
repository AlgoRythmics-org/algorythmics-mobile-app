package com.example.algorythmics.fragments.course

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.adapters.AlgorithmAdapter
import kotlinx.coroutines.launch
import androidx.fragment.app.viewModels
import com.example.algorythmics.databinding.FragmentCoursesBinding


class CoursesFragment : Fragment() {

    private val algorithmViewModel: CoursesListViewModel by viewModels()
    private lateinit var binding: FragmentCoursesBinding
    private val algorithmAdapter: AlgorithmAdapter by lazy { AlgorithmAdapter(emptyList()) }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = algorithmAdapter

        observeAlgorithmData()

        return view
    }

    private fun observeAlgorithmData() {
        lifecycleScope.launch {
            algorithmViewModel.loadAlgorithmData(requireContext())
            algorithmViewModel.algorithmModels.observe(viewLifecycleOwner) { algorithms ->
                algorithmAdapter.updateData(algorithms)
                for (algorithmModel in algorithms) {
                    Log.d("Algorithms", algorithmModel.toString())
                }
            }
        }
    }

}