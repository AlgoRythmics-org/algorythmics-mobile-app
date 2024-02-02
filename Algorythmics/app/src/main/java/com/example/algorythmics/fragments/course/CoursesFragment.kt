package com.example.algorythmics.fragments.course

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.algorythmics.R
import com.example.algorythmics.adapters.AlgorithmAdapter
import com.example.algorythmics.databinding.FragmentCoursesBinding
import com.example.algorythmics.retrofit.models.AlgorithmModel
import kotlinx.coroutines.launch

class CoursesFragment : Fragment() {

    private val algorithmViewModel: CoursesListViewModel by viewModels()
    private lateinit var binding: FragmentCoursesBinding
    private var algorithmAdapter: AlgorithmAdapter = AlgorithmAdapter(emptyList()) { algorithm -> onAlgorithmClicked(algorithm) }
    private val searchEditText: EditText by lazy { binding.searchEditText }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCoursesBinding.inflate(inflater, container, false)
        val view = binding.root

        val recyclerView: RecyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.setHasFixedSize(true)

        algorithmAdapter = AlgorithmAdapter(emptyList()) { algorithm -> onAlgorithmClicked(algorithm) }

        recyclerView.adapter = algorithmAdapter

        setupSearch()

        observeAlgorithmData()

        return view
    }

    private fun setupSearch() {
        searchEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                algorithmAdapter.filter(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                lifecycleScope.launch {
                    if (s.isNullOrBlank()) {
                        algorithmViewModel.algorithmModels.observe(viewLifecycleOwner) { algorithms ->
                            algorithmAdapter.updateData(algorithms)
                        }
                    } else {
                        val algorithms = algorithmViewModel.algorithmModels.value
                        val filteredAlgorithms = algorithms?.filter { it.matchesSearchQuery(s.toString()) } ?: emptyList()
                        algorithmAdapter.updateData(filteredAlgorithms)
                    }
                }
            }
        })
    }

    private fun observeAlgorithmData() {
        lifecycleScope.launch {
            algorithmViewModel.loadAlgorithmData(requireContext())
            algorithmViewModel.algorithmModels.observe(viewLifecycleOwner) { algorithms ->
                if (searchEditText.text.isNullOrBlank()) {
                    algorithmAdapter.updateData(algorithms)
                } else {
                    val filteredAlgorithms = algorithms.filter { it.matchesSearchQuery(searchEditText.text.toString()) }
                    algorithmAdapter.updateData(filteredAlgorithms)
                }

                for (algorithmModel in algorithms) {
                    Log.d("Algorithms", algorithmModel.toString())
                }
            }
        }
    }

    private fun onAlgorithmClicked(algorithm: AlgorithmModel) {
        val bundle = Bundle().apply {
            putString("algorithm", algorithm.algorithmId)
        }

        val detailFragment = CoursesDetailFragment().apply {
            arguments = bundle
        }

        parentFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, detailFragment) // Add instead of replace
            .hide(this@CoursesFragment) // Hide the current fragment
            .addToBackStack(null)
            .commit()
    }

    override fun onResume() {
        super.onResume()
        algorithmAdapter.filter(searchEditText.text.toString())
    }
}
