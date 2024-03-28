package com.example.algorythmics.fragments.course

import android.os.Bundle
import android.text.SpannableString
import android.text.style.ClickableSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.algorythmics.R
import com.example.algorythmics.adapters.SentenceAdapter
import com.example.algorythmics.adapters.WordsAdapter
import com.example.algorythmics.callback.DropListener
import com.example.algorythmics.databinding.FragmentCodeBinding
import com.google.android.flexbox.AlignItems
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent


class CodeFragment : Fragment() {
    private lateinit var binding: FragmentCodeBinding
    private val words = mutableListOf("world", "a", "!", "What", "wonderful")
    private var selectedWord = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCodeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val sentenceAdapter = SentenceAdapter()
        val wordsAdapter = WordsAdapter {
            selectedWord = it
        }.apply {
            submitList(words)
        }

        binding.rvSentence.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvSentence.adapter = sentenceAdapter

        binding.rvSentence.setOnDragListener(
            DropListener {
                wordsAdapter.removeItem(selectedWord)
                sentenceAdapter.addItem(selectedWord)
            }
        )

        binding.rvWords.layoutManager = FlexboxLayoutManager(requireContext(), FlexDirection.ROW, FlexWrap.WRAP).apply {
            justifyContent = JustifyContent.SPACE_EVENLY
            alignItems = AlignItems.CENTER
        }

        binding.rvWords.adapter = wordsAdapter
    }
}