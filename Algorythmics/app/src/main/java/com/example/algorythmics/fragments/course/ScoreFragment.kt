package com.example.algorythmics.fragments.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.algorythmics.R
import com.example.algorythmics.databinding.FragmentScoreBinding


class ScoreFragment : Fragment() {

    private lateinit var binding: FragmentScoreBinding
    private var score: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentScoreBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        score = arguments?.getInt("Score", 0) ?: 0

        binding.apply {
            scoreTxt.text = score.toString()
            backBtn.setOnClickListener {
                // Visszanavigálás a korábbi fragment-re
                requireActivity().onBackPressed()
            }
        }
    }
}