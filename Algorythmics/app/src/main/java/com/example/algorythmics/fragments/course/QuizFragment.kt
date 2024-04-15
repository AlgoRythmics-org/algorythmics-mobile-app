package com.example.algorythmics.fragments.course

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.algorythmics.R
import com.example.algorythmics.adapters.QuestionAdapter
import com.example.algorythmics.databinding.FragmentQuizBinding
import com.example.algorythmics.retrofit.models.QuizModel

class QuizFragment : Fragment(), QuestionAdapter.Score {

    private var _binding: FragmentQuizBinding? = null
    private val binding get() = _binding!!

    private var position: Int = 0
    private lateinit var receivedList: MutableList<QuizModel>
    private var allScore = 0

    private lateinit var questionAdapter: QuestionAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentQuizBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       // receivedList = arguments?.getParcelableArrayList<QuestionModel>("list")!!.toMutableList()

        receivedList = mutableListOf(
            QuizModel(4, "Kérdés 1", "Válasz 112", "Válasz 2", "Válasz 3", "Válasz 4", "a", 5, null),
            QuizModel(2, "Kérdés 12", "Válasz 1", "Válasz 2", "Válasz 3", "Válasz 4", "b", 5, null),
            QuizModel(3, "Kérdés 13", "Válaszok", "Válasz 2", "Válasz 3", "Válasz 4", "a", 5, null),
            QuizModel(4, "Kérdés 1", "Válasz 1", "Válasz 2", "Válasz 3", "Válasz 4", "d", 5, null),
            QuizModel(5, "Kérdés 1", "Válasz 1", "Válasz 2", "Válasz 3", "Válasz 4", "c", 5, null),
            QuizModel(6, "Kérdés 1", "Válasz 1", "Válasz 2", "Válasz 3", "Válasz 4", "a", 5, null),
            QuizModel(7, "Kérdés 1", "Válasz 1", "Válasz 2", "Válasz 3", "Válasz 4", "b", 5, null),
            QuizModel(8, "Kérdés 1", "Válasz 1", "Válasz 2", "Válasz 3", "Válasz 4", "c", 5, null),
            QuizModel(9, "Kérdés 1", "Válasz 1", "Válasz 2", "Válasz 3", "Válasz 4", "d", 5, null),
            QuizModel(10, "Kérdés 1", "Válasz 1", "Válasz 2", "Válasz 3", "Válasz 4", "a", 5, null)

        )
        setupUI()

        questionAdapter = QuestionAdapter("", mutableListOf(), this)
        binding.questionList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questionAdapter
        }
    }


    @SuppressLint("SetTextI18n")
    private fun setupUI() {
        binding.apply {
            backBtn.setOnClickListener { requireActivity().onBackPressed() }

            progressBar.apply {
                max = receivedList.size
                progress = 1
            }

            questionNumberTxt.text = "Question 1/${receivedList.size}"

            loadAnswers()

            rightArrow.setOnClickListener {
                if (progressBar.progress == receivedList.size) {
                    passScoreToScoreFragment()
                    return@setOnClickListener
                }
                position++
                progressBar.progress++
                questionNumberTxt.text = "Question ${progressBar.progress}/${receivedList.size}"
                questionTxt.text = receivedList[position].question
                loadAnswers()
            }

            leftArrow.setOnClickListener {
                if (progressBar.progress == 1) {
                    return@setOnClickListener
                }
                position--
                progressBar.progress--
                questionNumberTxt.text = "Question ${progressBar.progress}/${receivedList.size}"
                questionTxt.text = receivedList[position].question
                loadAnswers()
            }

            questionTxt.text = receivedList[position].question
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadAnswers(){
        val users: MutableList<String> = mutableListOf()
        users.add(receivedList[position].answer1.toString())
        users.add(receivedList[position].answer2.toString())
        users.add(receivedList[position].answer3.toString())
        users.add(receivedList[position].answer4.toString())

        if(receivedList[position].clickedAnswer != null)users.add(receivedList[position].clickedAnswer.toString())

        val questionAdapter by lazy{
            QuestionAdapter(
                receivedList[position].correctAnswer.toString(), users, this
            )
        }

        //questionAdapter.differ.submitList(users)
        binding.questionList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = questionAdapter
        }

        questionAdapter.differ.submitList(users)
    }

    override fun amount(number: Int, clickedAnswer: String) {
        allScore += number
        receivedList[position].clickedAnswer = clickedAnswer
    }

    private fun passScoreToScoreFragment() {

        val bundle = Bundle()
        bundle.putInt("score", allScore)
        val scoreFragment = ScoreFragment()

        scoreFragment.arguments = bundle

        parentFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, scoreFragment)
            .hide(this@QuizFragment)
            .addToBackStack(null)
            .commit()
    }

}