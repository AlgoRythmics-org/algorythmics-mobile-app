package com.example.algorythmics.fragments.course

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import com.example.algorythmics.R
import com.example.algorythmics.fragments.course.VideoFragment

class CoursesDetailFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_courses_detail, container, false)

        val videoCardView = view.findViewById<CardView>(R.id.video)
        val animationCardView = view.findViewById<CardView>(R.id.animation)
        val codeCardView = view.findViewById<CardView>(R.id.code)
        val quizCardView = view.findViewById<CardView>(R.id.quiz)

        videoCardView.setOnClickListener {
            //navigateToFragment(VideoFragment())
            val algorithmId = arguments?.getString("algorithmId")
            if (algorithmId != null) {
                val videoFragment = VideoFragment().apply {
                    arguments = Bundle().apply {
                        putString("algorithmId", algorithmId)
                    }
                }
                navigateToFragment(videoFragment)
            }
        }

        animationCardView.setOnClickListener {
            navigateToFragment(AnimationFragment())
        }

        codeCardView.setOnClickListener {
            navigateToFragment(CodeFragment())
        }

        quizCardView.setOnClickListener {
            navigateToFragment(QuizFragment())
        }

        return view
    }

    private fun navigateToFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, fragment, fragment.javaClass.simpleName)
        transaction.hide(this@CoursesDetailFragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

}